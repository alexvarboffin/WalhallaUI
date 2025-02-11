package com.walhalla.library

import android.app.Activity
import android.os.Handler
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class ScreenAdvertisement(val activity: Activity, val advertisementId: Int) {
    val adsHandler: Handler = Handler()

    //show the ads.
    private fun showAds() {
        // Show the ad.
        val adView = activity.findViewById<AdView>(
            advertisementId
        )
        adView.visibility = View.VISIBLE
        adView.isEnabled = true

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    //hide ads.
    private fun unshowAds() {
        val adView = activity.findViewById<AdView>(
            advertisementId
        )
        adView.visibility = View.INVISIBLE
        adView.isEnabled = false
    }

    val unshowAdsRunnable: Runnable = Runnable { unshowAds() }

    val showAdsRunnable: Runnable = Runnable { showAds() }

    fun showAdvertisement() {
        adsHandler.post(showAdsRunnable)
    }

    fun hideAdvertisement() {
        adsHandler.post(unshowAdsRunnable)
    }
}
