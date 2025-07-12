package com.video.maker.advertManager

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.video.maker.activity.VideoPreviewActivity
import com.video.maker.util.DLog
import com.walhalla.utils.AManagerI
import com.walhalla.utils.AManagerI.RewardManagerCallback
import com.walhalla.utils.AdmobAdsIds

class RewardManager private constructor() : AManagerI {
    private var callback: RewardManagerCallback? = null


    //    public void setCallback(RewardManagerCallback callback) {
    //        this.callback = callback;
    //    }
    private var m: AdmobAdsIds? = null

    fun createRewardedAd(activity: Activity, position: Int, _clb: RewardManagerCallback?) {
        this.callback = _clb
        //        AdManager.Ad_Popup(activity);
//        if (AdManager.REMOTE_AD.equals(REMOTE_AD)) {
//            DLog.d( "hiiiiiiiiiiiiiiii" + getResources().getString(R.string.Max_reword));
//            rewardedAd = MaxRewardedAd.getInstance(getResources().getString(R.string.Max_reword), activity);
//            rewardedAd.setListener(new MaxRewardedAdListener() {
//                @Override
//                public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {
//                    RatioFragment.this.onUserRewarded();
//                }
//
//                @Override
//                public void onRewardedVideoStarted(MaxAd maxAd) {
//
//                }
//
//                @Override
//                public void onRewardedVideoCompleted(MaxAd maxAd) {
//
//                }
//
//
//                @Override
//                public void onAdLoaded(MaxAd maxAd) {
//                    DLog.d( "hiiiiiii");
//                    retryAttempt = 0;
//                    if (rewardedAd.isReady()) {
//                        rewardedAd.showAd();
//                    }
//                    else
//                    {
//                        rewardedAd.loadAd();
//                    }
//                }
//                @Override
//                public void onAdDisplayed(MaxAd maxAd) {
//
//                }
//
//                @Override
//                public void onAdHidden(MaxAd maxAd) {
//
//                }
//
//                @Override
//                public void onAdClicked(MaxAd maxAd) {
//
//                }
//
//                @Override
//                public void onAdLoadFailed(String s, MaxError maxError) {
//                    DLog.d( "ffffff");
//                    retryAttempt++;
////                    AdManager.ProgressDialog.dismiss();
//                    long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            rewardedAd.loadAd();
//                        }
//                    }, delayMillis);
//                }
//
//                @Override
//                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
//                    DLog.d( "ffffff2");
//                    rewardedAd.loadAd();
////                    AdManager.ProgressDialog.dismiss();
//                }
//            });
//
//            rewardedAd.loadAd();
//        } else
        if (m is AdmobAdsIds) {
//            new Handler().postDelayed(() -> {
            showRewardAdBanner(activity, position, callback)
            //            },3000);
        }
    }


    fun init(m: AdmobAdsIds?) {
        this.m = m
    }

    var rewardedAd: RewardedAd? = null
    var isReward: Boolean = false

    override fun showRewardAdBanner(
        activity: Activity,
        position: Int,
        callback: RewardManagerCallback?
    ) {
        if (m == null || TextUtils.isEmpty(m!!.admob_reward_ad_id)) {
            return
        }

        //        AdManager.ProgressDialog.dismiss();
        if (rewardedAd == null) {
            loadRewardAd(activity)
        }

        if (rewardedAd != null) {
            rewardedAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(
                            TAG,
                            "Ad was clicked."
                        )
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()

                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        if (activity is VideoPreviewActivity) {
                            activity.onResumeVideo()
                        }
                        Log.d(
                            TAG,
                            "@@@ Ad dismissed fullscreen content. " + isReward + " " + (callback != null)
                        )
                        if (isReward) {
                            isReward = false
                            callback?.successResult7(position)
                            //load new
                            rewardedAd = null
                            loadRewardAd(activity) //ok
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when ad fails to show.
                        Log.e(
                            TAG,
                            "Ad failed to show fullscreen content."
                        )
                        rewardedAd = null
                        loadRewardAd(activity)
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(
                            TAG,
                            "Ad recorded an impression."
                        )
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(
                            TAG,
                            "Ad showed fullscreen content."
                        )
                    }
                }
            rewardedAd!!.show(
                activity
            ) { rewardItem: RewardItem? ->
                isReward = true
            }
        } else {
            callback!!.errorShowAds(position)
        }
    }

    override fun loadRewardAd(context: Context) {
        if (m == null || TextUtils.isEmpty(m!!.admob_reward_ad_id)) {
            return
        }

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, m!!.admob_reward_ad_id!!,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                    //                        AdManager.ProgressDialog.dismiss();
                    val code = loadAdError.code
                    DLog.d("ffff" + code + loadAdError.message + " " + loadAdError.responseInfo)
                    if (loadAdError.code == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                        Log.d(TAG, "-->0<--" + m!!.admob_reward_ad_id)
                    } else if (loadAdError.code == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                        Log.d(TAG, "-->2<--" + m!!.admob_reward_ad_id)
                    } else if (loadAdError.code == AdRequest.ERROR_CODE_NO_FILL) {
                        Log.d(TAG, "-->3<--" + m!!.admob_reward_ad_id)
                        //app-ads.txt
                        //"com.google.android.gms.ads"
                    } else if (code == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                        //errorReason = "Invalid request";
                    }
                    //loadRewardAd(context);
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad

                    //                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                            ...
//                        });
                    Log.d(TAG, "Ad was loaded.")
                }
            })
    }

    companion object {
        private const val TAG = "@@@"


        @JvmStatic
        @get:Synchronized
        var instance: RewardManager? = null
            get() {
                if (field == null) {
                    field = RewardManager()
                }
                return field
            }
            private set
    }
}
