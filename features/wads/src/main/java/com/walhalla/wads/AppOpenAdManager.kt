package com.walhalla.wads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.walhalla.library.BuildConfig
import com.walhalla.wads.DLog.d
import java.util.Date

/**
 * Inner class that loads and shows app open ads.
 */
class AppOpenAdManager(ad_unit_id: String?) {
    private val AD_UNIT_ID: String

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd: Boolean = false

    /**
     * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
     */
    private var loadTime: Long = 0

    /**
     * Constructor.
     */
    init {
        this.AD_UNIT_ID = if ((BuildConfig.DEBUG)) TEST_OPEN_AD_ID else ad_unit_id!!
    }

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    private fun loadAd(context: Context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable) {
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            AD_UNIT_ID,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = (Date()).time
                    d("onAdLoaded.")
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error.
                 */
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false

                    handleError(loadAdError)
                    d("onAdFailedToLoad: " + loadAdError.message)
                }
            })
    }

    private fun handleError(loadAdError: LoadAdError) {
        //свежий ID
        if (loadAdError.code == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
            Log.d(
                TAG,
                "-->0<--$AD_UNIT_ID"
            )
        } else if (loadAdError.code == AdRequest.ERROR_CODE_NETWORK_ERROR) {
            Log.d(
                TAG,
                "-->2<--$AD_UNIT_ID"
            )
        } else if (loadAdError.code == AdRequest.ERROR_CODE_NO_FILL) {
            Log.d(
                TAG,
                "-->3<--$AD_UNIT_ID"
            )
            //app-ads.txt
            //"com.google.android.gms.ads"
        }
        Log.d(TAG, loadAdError.toString())
    }


    /**
     * Check if ad was loaded more than n hours ago.
     */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = (Date()).time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        val mm = (dateDifference < (numMilliSecondsPerHour * numHours))

        d("@\uD83D\uDE80@isLoading=>$isLoadingAd :: $loadTime $dateDifference $mm")
        return mm
    }

    private val isAdAvailable: Boolean
        /**
         * Check if ad exists and can be shown.
         */
        get() =// Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    fun showAdIfAvailable(activity: Activity?) {
        //        if (activity == null) {
//            DLog.d("");
//            return;
//        }

        showAdIfAvailable(
            activity!!,
            object : OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }

                override fun adAdDismissedBackPressed() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            })
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                  the activity that shows the app open ad
     * @param onShowAdCompleteListener0 the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener0: OnShowAdCompleteListener) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            d("The app open ad is already showing.")
            return
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable) {
            d("The app open ad is not ready yet.")
            onShowAdCompleteListener0.onShowAdComplete()
            loadAd(activity)
            return
        }

        d("Will show ad.")

        appOpenAd!!.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    Log.d(
                        TAG,
                        "ww onAdClicked: "
                    )
                }


                override fun onAdImpression() {
                    Log.d(
                        TAG,
                        "ww onAdImpression: "
                    )
                }

                /** Called when full screen content is dismissed.  */
                override fun onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    onShowAdCompleteListener0.adAdDismissedBackPressed()
                    appOpenAd = null
                    isShowingAd = false
                    Log.d(
                        TAG,
                        "ww onAdDismissedFullScreenContent: "
                    )

                    loadAd(activity)
                }

                /** Called when fullscreen content failed to show.  */
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false

                    d("ww onAdFailedToShowFullScreenContent: " + adError.message)
                    onShowAdCompleteListener0.onShowAdComplete()
                    loadAd(activity)
                }

                /** Called when fullscreen content is shown.  */
                override fun onAdShowedFullScreenContent() {
                    d("ww onAdShowedFullScreenContent.")
                }
            }

        isShowingAd = true
        appOpenAd!!.show(activity)
    }

    companion object {
        private const val TAG = "@@@"
        private const val TEST_OPEN_AD_ID = "ca-app-pub-3940256099942544/9257395921"
    }
}