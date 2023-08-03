package com.walhalla.phonenumber.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.walhalla.phonenumber.R;
import com.walhalla.phonenumber.Utils;
import com.walhalla.phonenumber.apps.adapter.AppAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class WalhallaAppListFragment extends Fragment implements SortedListAdapter.Callback {
    private final List<WordModel> mModels = new ArrayList<>();

    private static final Comparator<WordModel> COMPARATOR
            = new SortedListAdapter.ComparatorBuilder<WordModel>()
            .setOrderForModel(WordModel.class, (a, b) -> Integer.signum(a.getRank() - b.getRank()))
            .build();


    private static final String KEY_VAR0 = WalhallaAppListFragment.class.getSimpleName();
    private String fileName;

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
            WalhallaAppAdapter configAdapter = new WalhallaAppAdapter(getActivity(), COMPARATOR, model -> {
                final String message = "" + model.toString();
                {
                    //Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
                }
            });
            configAdapter.addCallback(this);
            recyclerView.setAdapter(configAdapter);
            ArrayList<WordModel> aa = Utils.oBuffers(getContext());
            for (WordModel topic : aa) {
                // Проверка установки приложения по пакету
                String mm = "com.walhalla." + topic.q;
                if (topic.q.contains(".")) {
                    mm = topic.q;
                }

                boolean isInstalled = Utils.isAppInstalled(getContext(), mm);
                topic.isInstalled = isInstalled;
                //DLog.d("@" + mm+" "+topic.isInstalled);
                mModels.add(topic);
            }
            configAdapter.edit()
                    .replaceAll(mModels)
                    .commit();
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
