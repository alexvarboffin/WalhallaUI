package com.walhalla.library

import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

//import static android.accounts.AccountManager.VISIBILITY_UNDEFINED;
class InterAdListener(private val interstitialAd: InterstitialAd) : AdListener() {
    override fun onAdClosed() {
        super.onAdClosed()
        Log.d(TAG, "onAdClosed: " + interstitialAd.adUnitId)
    }


    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
        super.onAdFailedToLoad(loadAdError)
        if (DEBUG) {
            Log.d(
                TAG, String.format(
                    "Ad %s failed to load with error %d.",
                    interstitialAd.adUnitId, loadAdError.code
                )
            )
        }
    }

    override fun onAdOpened() {
        super.onAdOpened()
        Log.d(TAG, "onAdOpened: " + interstitialAd.adUnitId)
    }


    //    @Override
    //    public void onAdLeftApplication() {
    //        super.onAdLeftApplication();
    //        Log.d(TAG, "onAdLeftApplication: " + interstitialAd.getAdUnitId());
    //    }
    override fun onAdLoaded() {
        super.onAdLoaded()
        Log.d(TAG, "onAdLoaded: " + interstitialAd.adUnitId)
        //interstitialAd.show();
    }

    override fun onAdClicked() {
        super.onAdClicked()
        Log.d(TAG, "onAdClicked: " + interstitialAd.adUnitId)
    }

    override fun onAdImpression() {
        super.onAdImpression()
        Log.d(TAG, "onAdImpression: " + interstitialAd.adUnitId)
    }

    companion object {
        private const val TAG = "@@@"
        private val DEBUG = BuildConfig.DEBUG
    }
}
