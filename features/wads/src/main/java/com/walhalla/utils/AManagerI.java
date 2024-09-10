package com.walhalla.utils;

import android.app.Activity;
import android.content.Context;

public interface AManagerI {

    interface RewardManagerCallback {
        void successResult7(int position);

        void errorShowAds();

        //void onResumeVideo();
    }

    void loadRewardAd(Context context);

    void showRewardAdBanner(Activity activity, int position, RewardManagerCallback callback);
}
