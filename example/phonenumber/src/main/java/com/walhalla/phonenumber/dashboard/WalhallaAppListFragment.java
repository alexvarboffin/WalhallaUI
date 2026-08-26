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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.walhalla.phonenumber.R;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class WalhallaAppListFragment extends Fragment implements SortedListAdapter.Callback {

    private static final String TAG = "WalhallaFB";
    private static final long LOAD_TIMEOUT_MS = 15_000L;

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
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Runnable timeoutRunnable = () -> {
        if (!dataReceived) {
            Log.e(TAG, "TIMEOUT: no onDataChange after " + LOAD_TIMEOUT_MS + "ms");
            onRetrievalFailed("Firebase timeout (check INTERNET / RTDB rules)");
        }
    };
    private final ValueEventListener connectedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Boolean connected = snapshot.getValue(Boolean.class);
            Log.i(TAG, ".info/connected = " + connected);
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
            bindSnapshot(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            dataReceived = true;
            mainHandler.removeCallbacks(timeoutRunnable);
            Log.e(TAG, "onCancelled code=" + error.getCode()
                    + " message=" + error.getMessage()
                    + " details=" + error.getDetails());
            onRetrievalFailed(error.getMessage());
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
                onRetrievalFailed("Firebase ref is null");
                return;
            }
            if (listenerAttached) {
                Log.d(TAG, "loadData: listener already attached, skip");
                return;
            }
            dataReceived = false;
            if (connectedRef != null) {
                connectedRef.addValueEventListener(connectedListener);
            }
            Log.i(TAG, "loadData: addValueEventListener on " + databaseReference);
            databaseReference.addValueEventListener(clb);
            listenerAttached = true;
            mainHandler.postDelayed(timeoutRunnable, LOAD_TIMEOUT_MS);

            Task<DataSnapshot> getTask = databaseReference.get();
            getTask.addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    Log.e(TAG, "get() failed", e);
                    return;
                }
                DataSnapshot snap = task.getResult();
                Log.i(TAG, "get() ok exists=" + (snap != null && snap.exists())
                        + " children=" + (snap != null ? snap.getChildrenCount() : -1));
            });
        } catch (Exception e) {
            Log.e(TAG, "loadData failed", e);
            onRetrievalFailed(e.getLocalizedMessage());
        }
    }

    private void bindSnapshot(@NonNull DataSnapshot snapshot) {
        List<WordModel> mModels = new ArrayList<>();
        int skipped = 0;
        for (DataSnapshot obj : snapshot.getChildren()) {
            try {
                Log.d(TAG, "child key=" + obj.getKey() + " value=" + obj.getValue());
                AppModel model = obj.getValue(AppModel.class);
                if (model == null || TextUtils.isEmpty(model.q)) {
                    skipped++;
                    Log.w(TAG, "skip child key=" + obj.getKey() + " model=" + model
                            + " q=" + (model != null ? model.q : null));
                    continue;
                }

                String mm = "com.walhalla." + model.q;
                if (model.q.contains(".")) {
                    mm = model.q;
                }

                String isInstalled0 = getAppVersion(getContext(), mm);
                boolean isInstalled = isInstalled0 != null;
                if (isInstalled) {
                    String m0m = isInstalled0.split(".release")[0];
                    String[] nn = m0m.split("\\.");
                    model.time = getColorForDate(nn[nn.length - 1]);
                } else {
                    model.time = R.color.versionRed;
                }
                model.isInstalled = isInstalled;
                Log.d(TAG, "ok #" + mModels.size()
                        + " q=" + model.q
                        + " pkg=" + mm
                        + " installed=" + isInstalled
                        + " ver=" + isInstalled0
                        + " project=" + model.projectName);
                mModels.add(model);

            } catch (Exception e) {
                skipped++;
                Log.e(TAG, "parse failed key=" + obj.getKey(), e);
                DLog.handleException(e);
            }
        }
        Log.i(TAG, "parsed ok=" + mModels.size() + " skipped=" + skipped);
        if (!mModels.isEmpty()) {
            onMessageRetrieved(mModels);
        } else {
            onRetrievalFailed("Database is empty (children="
                    + snapshot.getChildrenCount() + ", skipped=" + skipped + ")");
        }
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
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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
