package com.walhalla.phonenumber.dashboard;

import static com.walhalla.phonenumber.dashboard.DateColorUtils.getColorForDate;
import static com.walhalla.ui.utils.PackageUtils.getAppVersion;

import android.os.Bundle;
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
import com.walhalla.phonenumber.R;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class WalhallaAppListFragment extends Fragment implements SortedListAdapter.Callback {
    //private final List<WordModel> mModels = new ArrayList<>();

    private static final Comparator<WordModel> COMPARATOR
            = new SortedListAdapter.ComparatorBuilder<WordModel>()
            .setOrderForModel(WordModel.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();


    private static final String KEY_VAR0 = WalhallaAppListFragment.class.getSimpleName();
    private String fileName;
    private DatabaseReference databaseReference;
    private WalhallaAppAdapter configAdapter;
    private ValueEventListener clb = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //showLoader();
//                            if (mainCallback != null) {
//                                mainCallback.hideLoader();
//                            }
            List<WordModel> mModels = new ArrayList<>();
            for (DataSnapshot obj : snapshot.getChildren()) {
                try {
                    AppModel model = obj.getValue(AppModel.class);

                    DLog.d("@@@@" + model.q);
//                                    ArrayList<WordModel> aa = Utils.oBuffers(getContext());
//                                    for (WordModel model : aa) {
                    // Проверка установки приложения по пакету
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
                    //DLog.d("@" + mm+" "+model.isInstalled);
                    mModels.add(model);
                    //}

                } catch (Exception e) {
                    DLog.handleException(e);
                    onRetrievalFailed(
                            "Failed to getUrl value." + e.getLocalizedMessage());
                }
            }
            if (!mModels.isEmpty()) {
                onMessageRetrieved(mModels);
            } else {
                onRetrievalFailed("Database is empty, reinstall the Application");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
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
        databaseReference = FirebaseDatabase.getInstance().getReference("walhalla");

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

        if (savedInstanceState == null) {
            configAdapter = new WalhallaAppAdapter(getActivity(), COMPARATOR, model -> {
                final String message = "" + model.toString();
                {
                    //Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
                }
            });
            configAdapter.addCallback(this);
            recyclerView.setAdapter(configAdapter);
            loadData();

        }
    }

    private void loadData() {
        try {
            databaseReference
                    //.child("/")
                    .addValueEventListener(clb);

            //reference.addChildEventListener(childEventListener);
        } catch (Exception e) {
            onRetrievalFailed(e.getLocalizedMessage());
        }

    }

    private void onMessageRetrieved(List<WordModel> tmp) {
        configAdapter.edit()
                .removeAll()
                .commit();
        configAdapter.edit()
                .replaceAll(tmp)
                .commit();
    }

    private void onRetrievalFailed(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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
