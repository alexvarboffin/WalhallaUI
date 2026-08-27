package com.walhalla.phonenumber.dashboard;

import static com.walhalla.phonenumber.dashboard.DateColorUtils.getColorForDate;
import static com.walhalla.ui.utils.PackageUtils.getAppVersion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walhalla.phonenumber.R;
import com.walhalla.ui.DLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WalhallaAppListFragment extends Fragment implements SortedListAdapter.Callback {

    private static final String TAG = "WalhallaFB";
    private static final long LOAD_TIMEOUT_MS = 8_000L;
    private static final String REST_URL =
            "https://landingj0bar-default-rtdb.firebaseio.com/walhalla.json";
    private static final String ASSET_FALLBACK = "walhalla.json";

    private static final Comparator<WordModel> COMPARATOR
            = new SortedListAdapter.ComparatorBuilder<WordModel>()
            .setOrderForModel(WordModel.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();


    private static final String KEY_VAR0 = WalhallaAppListFragment.class.getSimpleName();
    private String fileName;
    private DatabaseReference databaseReference;
    private DatabaseReference connectedRef;
    private WalhallaAppAdapter configAdapter;
    private boolean listenerAttached;
    private boolean dataReceived;
    private boolean fallbackStarted;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Gson gson = new Gson();
    private final Runnable timeoutRunnable = () -> {
        if (!dataReceived) {
            Log.e(TAG, "TIMEOUT: websocket idle, trying REST/asset fallback");
            startFallback("websocket timeout");
        }
    };
    private final ValueEventListener connectedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Boolean connected = snapshot.getValue(Boolean.class);
            Log.i(TAG, ".info/connected = " + connected);
            if (Boolean.FALSE.equals(connected) && !dataReceived) {
                // give WS a moment, then REST will run on timeout; log hint early
                Log.w(TAG, "RTDB not connected yet (VPN or deactivated DB?)");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, ".info/connected cancelled: " + error.getMessage());
        }
    };
    private final ValueEventListener clb = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            dataReceived = true;
            mainHandler.removeCallbacks(timeoutRunnable);
            Log.i(TAG, "onDataChange path=" + snapshot.getRef()
                    + " exists=" + snapshot.exists()
                    + " children=" + snapshot.getChildrenCount()
                    + " key=" + snapshot.getKey());
            bindModels(fromSnapshot(snapshot), "firebase-ws");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "onCancelled code=" + error.getCode()
                    + " message=" + error.getMessage()
                    + " details=" + error.getDetails());
            startFallback("ws cancelled: " + error.getMessage());
        }
    };


    public static Fragment newInstance(String s) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_VAR0, s);
        WalhallaAppListFragment fragment = new WalhallaAppListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString(KEY_VAR0);
        }
        logNetworkState();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        try {
            db.goOnline();
        } catch (Exception e) {
            Log.w(TAG, "goOnline failed", e);
        }
        databaseReference = db.getReference("walhalla");
        connectedRef = db.getReference(".info/connected");
        Log.i(TAG, "onCreate ref=" + databaseReference
                + " app=" + db.getApp().getName()
                + " options=" + db.getApp().getOptions().getDatabaseUrl());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        if (configAdapter == null) {
            configAdapter = new WalhallaAppAdapter(getActivity(), COMPARATOR, model -> {
                Log.d(TAG, "click " + model.q);
            });
            configAdapter.addCallback(this);
        }
        recyclerView.setAdapter(configAdapter);
        Log.i(TAG, "onViewCreated adapter set, loading Firebase…");
        loadData();
    }

    private void loadData() {
        try {
            if (databaseReference == null) {
                Log.e(TAG, "loadData: databaseReference is null");
                startFallback("ref null");
                return;
            }
            if (listenerAttached) {
                Log.d(TAG, "loadData: listener already attached, skip");
                return;
            }
            dataReceived = false;
            fallbackStarted = false;
            if (connectedRef != null) {
                connectedRef.addValueEventListener(connectedListener);
            }
            Log.i(TAG, "loadData: addValueEventListener on " + databaseReference);
            databaseReference.addValueEventListener(clb);
            listenerAttached = true;
            mainHandler.postDelayed(timeoutRunnable, LOAD_TIMEOUT_MS);
        } catch (Exception e) {
            Log.e(TAG, "loadData failed", e);
            startFallback(e.getLocalizedMessage());
        }
    }

    private void startFallback(String reason) {
        if (fallbackStarted || dataReceived) {
            return;
        }
        fallbackStarted = true;
        mainHandler.removeCallbacks(timeoutRunnable);
        Log.w(TAG, "fallback start reason=" + reason);
        io.execute(() -> {
            String restError = null;
            try {
                Log.i(TAG, "REST GET " + REST_URL);
                HttpURLConnection conn = (HttpURLConnection) new URL(REST_URL).openConnection();
                conn.setConnectTimeout(10_000);
                conn.setReadTimeout(10_000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                InputStream stream = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
                String body = readFully(stream);
                Log.i(TAG, "REST http=" + code + " body=" + truncate(body, 300));
                if (code == 200 && body != null && !body.equals("null")) {
                    List<AppModel> models = parseJsonList(body);
                    mainHandler.post(() -> {
                        dataReceived = true;
                        bindModels(enrich(models), "firebase-rest");
                    });
                    return;
                }
                restError = "REST http=" + code + " " + truncate(body, 160);
            } catch (Exception e) {
                restError = e.getMessage();
                Log.e(TAG, "REST failed", e);
            }

            final String err = restError;
            try {
                Log.w(TAG, "loading asset fallback " + ASSET_FALLBACK + " after: " + err);
                Context ctx = getContext();
                if (ctx == null) {
                    mainHandler.post(() -> onRetrievalFailed("No context for asset fallback"));
                    return;
                }
                String body = readFully(ctx.getAssets().open(ASSET_FALLBACK));
                List<AppModel> models = parseJsonList(body);
                mainHandler.post(() -> {
                    dataReceived = true;
                    Toast.makeText(getContext(),
                            "Firebase RTDB offline → assets/" + ASSET_FALLBACK,
                            Toast.LENGTH_LONG).show();
                    bindModels(enrich(models), "assets");
                });
            } catch (Exception e) {
                Log.e(TAG, "asset fallback failed", e);
                mainHandler.post(() -> onRetrievalFailed(
                        (err != null ? err : "Firebase failed") + " | asset: " + e.getMessage()));
            }
        });
    }

    private List<AppModel> parseJsonList(String body) throws Exception {
        body = body.trim();
        Type listType = new TypeToken<List<AppModel>>() {
        }.getType();
        if (body.startsWith("[")) {
            List<AppModel> list = gson.fromJson(body, listType);
            return list != null ? list : new ArrayList<>();
        }
        // object map { "0": {...}, "1": {...} } or keyed by package
        JSONObject obj = new JSONObject(body);
        if (obj.has("error")) {
            throw new IllegalStateException(obj.optString("error"));
        }
        List<AppModel> out = new ArrayList<>();
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object val = obj.get(key);
            if (val instanceof JSONObject) {
                AppModel m = gson.fromJson(val.toString(), AppModel.class);
                if (m != null) {
                    out.add(m);
                }
            } else if (val instanceof JSONArray) {
                // shouldn't happen at top for map, ignore
            }
        }
        return out;
    }

    private List<WordModel> fromSnapshot(@NonNull DataSnapshot snapshot) {
        List<WordModel> mModels = new ArrayList<>();
        for (DataSnapshot obj : snapshot.getChildren()) {
            try {
                AppModel model = obj.getValue(AppModel.class);
                if (model == null || TextUtils.isEmpty(model.q)) {
                    Log.w(TAG, "skip child key=" + obj.getKey());
                    continue;
                }
                mModels.add(model);
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
        return enrich(mModels);
    }

    private List<WordModel> enrich(List<? extends WordModel> raw) {
        List<WordModel> mModels = new ArrayList<>();
        Context ctx = getContext();
        for (WordModel model : raw) {
            if (model == null || TextUtils.isEmpty(model.q)) {
                continue;
            }
            if (!(model instanceof AppModel)) {
                // gson may give AppModel already
            }
            String mm = model.q.contains(".") ? model.q : ("com.walhalla." + model.q);
            String isInstalled0 = ctx != null ? getAppVersion(ctx, mm) : null;
            boolean isInstalled = isInstalled0 != null;
            if (isInstalled) {
                try {
                    String m0m = isInstalled0.split(".release")[0];
                    String[] nn = m0m.split("\\.");
                    model.time = getColorForDate(nn[nn.length - 1]);
                } catch (Exception e) {
                    model.time = R.color.versionRed;
                }
            } else {
                model.time = R.color.versionRed;
            }
            model.isInstalled = isInstalled;
            Log.d(TAG, "ok q=" + model.q + " pkg=" + mm + " installed=" + isInstalled);
            mModels.add(model);
        }
        return mModels;
    }

    private void bindModels(List<WordModel> mModels, String source) {
        Log.i(TAG, "bind source=" + source + " size=" + mModels.size());
        if (!mModels.isEmpty()) {
            onMessageRetrieved(mModels);
        } else {
            onRetrievalFailed("Empty list from " + source);
        }
    }

    private static String readFully(InputStream in) throws Exception {
        if (in == null) {
            return "";
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "null";
        }
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }

    private void logNetworkState() {
        Context ctx = getContext();
        if (ctx == null) {
            Log.w(TAG, "network: context null");
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Log.w(TAG, "network: ConnectivityManager null");
            return;
        }
        NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
        Log.i(TAG, "network caps=" + caps
                + " internet=" + (caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                + " validated=" + (caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)));
    }

    @Override
    public void onDestroy() {
        mainHandler.removeCallbacks(timeoutRunnable);
        if (connectedRef != null) {
            connectedRef.removeEventListener(connectedListener);
        }
        if (listenerAttached && databaseReference != null) {
            Log.d(TAG, "onDestroy: removeEventListener");
            databaseReference.removeEventListener(clb);
            listenerAttached = false;
        }
        io.shutdownNow();
        super.onDestroy();
    }

    private void onMessageRetrieved(List<WordModel> tmp) {
        Log.i(TAG, "onMessageRetrieved size=" + tmp.size()
                + " adapter=" + (configAdapter != null));
        if (configAdapter == null) {
            Log.e(TAG, "adapter null, cannot bind list");
            return;
        }
        configAdapter.edit()
                .removeAll()
                .commit();
        configAdapter.edit()
                .replaceAll(tmp)
                .commit();
        Log.i(TAG, "adapter updated, itemCount=" + configAdapter.getItemCount());
    }

    private void onRetrievalFailed(String s) {
        Log.e(TAG, "onRetrievalFailed: " + s);
        if (getContext() != null) {
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onEditStarted() {
    }

    @Override
    public void onEditFinished() {
    }
}
