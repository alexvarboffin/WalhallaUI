package com.walhalla.library

import android.content.Context
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.walhalla.library.Const.testDevices
import java.util.UUID

object AdMobCase {
    @JvmStatic
    fun attachToTop(id: Int): RelativeLayout.LayoutParams {
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layoutParams.addRule(RelativeLayout.ABOVE, id) //not work if set ALIGN_PARENT_TOP
        return layoutParams
    }


    @JvmStatic
    fun attachToBottom(id: Int): RelativeLayout.LayoutParams {
        val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layoutParams.addRule(RelativeLayout.BELOW, id) //not work if set ALIGN_PARENT_TOP
        return layoutParams
    }


    @JvmStatic
    fun createBanner(context: Context, banner_ad_unit_id: String): AdView {
        val id = UUID.randomUUID().hashCode()
        val mAdView = AdView(context)
        //mAdView.setVisibility(View.GONE);
        mAdView.setAdSize(AdSize.BANNER)
        mAdView.adUnitId = banner_ad_unit_id
        mAdView.id = id
        return mAdView
    }


    //#########
    //admob
    //########
    @JvmStatic
    fun interstitialBannerRequest(mAdView: AdView) {
        // Start loading the ad in the background.
        mAdView.adListener = AdListener(mAdView)
        mAdView.loadAd(buildAdRequest())
    }

    //    public static void interstitialBannerRequest(@NonNull InterstitialAd interstitialAd) {
    //        //Log.i(TAG, "interstitialBannerRequest: ");
    //        // Start loading the ad in the background.
    //        interstitialAd.setAdListener(new AdListener(interstitialAd));
    //        interstitialAd.loadAd(AdMobCase.buildAdRequest());
    //    }
    fun buildAdRequest(): AdRequest {
        val adRequest = AdRequest.Builder()
        val arr = testDevices()
        //        if (arr != null) {
//            for (String device : arr) {
//                adRequest.addTestDevice(device);
//            }
//        }
        return adRequest.build()
    }
    /**
     * show ads
     *
     * if (config.interstitial_ad_unit_id != null) {
     * // Create the InterstitialAd and set the adUnitId.
     * InterstitialAd mInterstitialAd = new InterstitialAd(a);
     * // Defined in res/values/strings.xml
     * mInterstitialAd.setAdUnitId(config.interstitial_ad_unit_id);
     * //prepare ads
     * AdRequest adRequest = new AdRequest.Builder().build();
     * mInterstitialAd.loadAd(adRequest);
     * mInterstitialAdList.add(mInterstitialAd);
     * }
     */
    //    public void showInterstitial() {
    //        // Show the ad if it's ready
    //        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
    //            mInterstitialAd.show();
    //        }
    //    }
}
