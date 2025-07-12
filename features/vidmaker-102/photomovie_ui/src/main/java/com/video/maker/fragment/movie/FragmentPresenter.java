package com.video.maker.fragment.movie;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.video.maker.activity.VideoPreviewActivity;
import com.video.maker.advertManager.RewardManager;
import com.video.maker.databinding.DialogLayoutBinding;


public abstract class FragmentPresenter extends Fragment {


    //MaxRewardedAd rewardedAd;
    int retryAttempt;
    protected RewardManager rm;




    public void showPopupDialog(Activity activity, View.OnClickListener clb) {
        if (activity instanceof VideoPreviewActivity) {
            ((VideoPreviewActivity) activity).onPauseVideo();
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        DialogLayoutBinding binding = DialogLayoutBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.dPurchase.setOnClickListener(v -> {
            alertDialog.dismiss();
            clb.onClick(v);
        });
        binding.dCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
            VideoPreviewActivity videoActivity = (VideoPreviewActivity) getActivity();
            if (videoActivity != null) {
                videoActivity.onResumeVideo();
            }
        });
        alertDialog.show();
    }

//    void onUserRewardedRequest0() {
//        VideoPreviewActivity videoActivity = (VideoPreviewActivity) getActivity();
//        if (videoActivity != null) {
//            videoActivity.onResumeVideo();
//        }
//        successResult7();
//    }
}
