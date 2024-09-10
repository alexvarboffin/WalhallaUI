package com.walhalla.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardManager implements AManagerI {

    private RewardManagerCallback mainCallback;

    private static final String TAG = "@@@";
    private RewardedAd rewardedAd;

    private static RewardManager instance;
    private boolean isReward;


    private RewardManager() {
    }


    public static synchronized RewardManager getInstance() {
        if (instance == null) {
            instance = new RewardManager();
        }
        return instance;
    }

    private AdmobAdsIds adsIds;

    public void init(AdmobAdsIds m) {
        this.adsIds = m;
    }

    @Override
    public void loadRewardAd(Context context) {

        if (adsIds == null || TextUtils.isEmpty(adsIds.admob_reward_ad_id)) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, adsIds.admob_reward_ad_id,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }


    @Override
    public void showRewardAdBanner(Activity activity, int position, RewardManagerCallback callback) {

        if (adsIds == null || TextUtils.isEmpty(adsIds.admob_reward_ad_id)) {
            return;
        }

        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.

//                    if (activity instanceof VideoPreviewActivity) {
//                        ((VideoPreviewActivity) activity).onResumeVideo();
//                    }
                    Log.d(TAG, "@@@ Ad dismissed fullscreen content. " + isReward + " " + (callback != null));
                    if (isReward) {
                        isReward = false;
                        if (callback != null) {
                            callback.successResult7(position);
                        }
                        //load new
                        rewardedAd = null;
                        loadRewardAd(activity);//ok
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    rewardedAd = null;
                    loadRewardAd(activity);
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });
            rewardedAd.show(activity, rewardItem -> {
                isReward = true;
                //unlock....
            });
        } else {
            if (callback != null) {
                callback.errorShowAds();
            }
        }
    }
}
