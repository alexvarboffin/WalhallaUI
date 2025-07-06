package com.walhalla.utils

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
import com.walhalla.utils.AManagerI.RewardManagerCallback

class RewardManager private constructor() : AManagerI {
    private val mainCallback: RewardManagerCallback? = null

    private var rewardedAd: RewardedAd? = null

    private var isReward = false


    private var adsIds: AdmobAdsIds? = null

    fun init(m: AdmobAdsIds?) {
        this.adsIds = m
    }

    override fun loadRewardAd(context: Context) {
        val admobId = adsIds?.admob_reward_ad_id ?: return

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, admobId,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                    Log.d(TAG, loadAdError.toString())
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d(TAG, "Ad was loaded.")
                }
            })
    }


    override fun showRewardAdBanner(
        activity: Activity,
        position: Int,
        callback: RewardManagerCallback?
    ) {
        if (adsIds == null || TextUtils.isEmpty(adsIds!!.admob_reward_ad_id)) {
            return
        }

        if (rewardedAd != null) {
            rewardedAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.

                        //                    if (activity instanceof VideoPreviewActivity) {
                        //                        ((VideoPreviewActivity) activity).onResumeVideo();
                        //                    }

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
            callback?.errorShowAds()
        }
    }

    companion object {
        private const val TAG = "@@@"

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
