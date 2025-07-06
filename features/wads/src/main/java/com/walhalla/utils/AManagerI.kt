package com.walhalla.utils

import android.app.Activity
import android.content.Context

interface AManagerI {
    interface RewardManagerCallback {
        fun successResult7(position: Int)

        fun errorShowAds() //void onResumeVideo();
    }

    fun loadRewardAd(context: Context)

    fun showRewardAdBanner(activity: Activity, position: Int, callback: RewardManagerCallback?)
}
