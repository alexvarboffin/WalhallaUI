package ai.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import ai.FileUtils;
import ai.SvgAdapter;
import ai.databinding.ActivityLottieBinding;

public class SvgFragment extends Fragment {

    private ActivityLottieBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityLottieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager lp = new GridLayoutManager(getContext(), 2);
        binding.recyclerView.setLayoutManager(lp);
        SvgAdapter adapter = new SvgAdapter(FileUtils.getLottieAnimations(getContext(), "svg"), getContext());
        binding.recyclerView.setAdapter(adapter);
    }
}
