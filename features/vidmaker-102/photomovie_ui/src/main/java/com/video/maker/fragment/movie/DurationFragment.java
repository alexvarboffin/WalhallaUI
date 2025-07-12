package com.video.maker.fragment.movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.video.maker.R;
import com.video.maker.databinding.FragmentMovieDurationBinding;


public class DurationFragment extends Fragment {
    DurationFragmentListener listener;

    private static final int DURATION_1_SECOND = 1000;
    private static final int DURATION_2_SECONDS = 2000;
    private static final int DURATION_3_SECONDS = 3000;
    private static final int DURATION_5_SECONDS = 5000;
    public static final int DURATION_7_SECONDS = 7000;
    private static final int DURATION_10_SECONDS = 10000;


    public interface DurationFragmentListener {
        void onDurationSelect(int i);
    }

    public void setDurationFragmentListener(DurationFragmentListener durationFragmentListener) {
        this.listener = durationFragmentListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        @NonNull FragmentMovieDurationBinding binding = FragmentMovieDurationBinding.inflate(inflater, viewGroup, false);
        binding.rd7.setChecked(true);
        binding.rdgDuration.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (DurationFragment.this.listener != null) {
                if (i == R.id.rd10) {
                    DurationFragment.this.listener.onDurationSelect(DURATION_1_SECOND);
                    return;
                } else if (i == R.id.rd15) {
                    DurationFragment.this.listener.onDurationSelect(DURATION_2_SECONDS);
                    return;
                } else if (i == R.id.rd20) {
                    DurationFragment.this.listener.onDurationSelect(DURATION_3_SECONDS);
                    return;
                } else if (i == R.id.rd25) {
                    DurationFragment.this.listener.onDurationSelect(DURATION_5_SECONDS);
                    return;
                } else if (i == R.id.rd7) {
                    DurationFragment.this.listener.onDurationSelect(DURATION_7_SECONDS);
                    return;
                } else if (i == R.id.rd30) {
                    DurationFragment.this.listener.onDurationSelect(DURATION_10_SECONDS);
                    return;
                }
            } else {
                Toast.makeText(DurationFragment.this.getActivity(), DurationFragment.this.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }
}
