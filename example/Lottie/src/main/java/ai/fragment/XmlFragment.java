package ai.fragment;

import static ai.FileUtils.loadXmlFromAssets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import ai.FileUtils;
import ai.adapter.SvgAdapter;
import ai.adapter.XmlAdapter;
import ai.databinding.FragmentLottieBinding;


public class XmlFragment extends Fragment {

    private FragmentLottieBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLottieBinding.inflate(inflater, container, false);

        // RecyclerView setup
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        XmlAdapter adapter = new XmlAdapter(getContext(), loadXmlFromAssets(getContext(), "xml"));

        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
