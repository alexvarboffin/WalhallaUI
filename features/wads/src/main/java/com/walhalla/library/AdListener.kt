package com.walhalla.library

import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.walhalla.wads.DLog

class AdListener //Constructor #1
    (private val mObject: Any) : AdListener() {
    override fun onAdClosed() {
        super.onAdClosed()
        if (DEBUG) {
            if (mObject is AdView) {
                DLog.d("onAdClosed: " + mObject.adUnitId)
            } else if (mObject is InterstitialAd) {
                DLog.d("onAdClosed: " + mObject.adUnitId)

                // Load the next interstitial.
                //interstitialAd.loadAd(AdMobCase.buildAdRequest());
                //interstitialAd = new InterstitialAd(a);
            }
        }
    }


    override fun onAdFailedToLoad(error: LoadAdError) {
        super.onAdFailedToLoad(error)
        if (DEBUG) {
            var errorReason = ""
            val code = error.code
            if (code == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                errorReason = "Internal error"
            } else if (code == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                errorReason = "Invalid request"
            } else if (code == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                errorReason = "Network Error"
                /*
                 * The ad request was successful, but no ad was returned due to lack of ad inventory.
                 * */
            } else if (code == AdRequest.ERROR_CODE_NO_FILL) {
                errorReason = "No fill"

                //                PACKAGE_NAME_KEY_LEGACY_VISIBLE}, //#{@link #PACKAGE_NAME_KEY_LEGACY_NOT_VISIBLE
//                case VISIBILITY_UNDEFINED:
//                    errorReason = "onAdFailedToLoad: VISIBILITY_UNDEFINED";
            }

            if (mObject is AdView) {
                DLog.d(
                    String.format(
                        "Ad %s failed to load with error %s.",
                        mObject.adUnitId,
                        errorReason
                    )
                )

                if (error.code == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                    if (mObject.visibility == View.VISIBLE) {
                        mObject.visibility = View.GONE
                    }
                }
            } else if (mObject is InterstitialAd) {
                if (DEBUG) {
                    DLog.d(
                        String.format(
                            "Ad %s failed to load with error %s.",
                            mObject.adUnitId,
                            errorReason
                        )
                    )
                }
            }
        }
    }


    override fun onAdOpened() {
        super.onAdOpened()
        if (mObject is AdView) {
            DLog.d("onAdOpened: " + mObject.adUnitId)
        } else if (mObject is InterstitialAd) {
            DLog.d("onAdOpened: " + mObject.adUnitId)
        }
    }


    fun onAdLeftApplication() {
        //super.onAdLeftApplication();
        if (mObject is AdView) {
            DLog.d("onAdLeftApplication: " + mObject.adUnitId)
        } else if (mObject is InterstitialAd) {
            DLog.d("onAdLeftApplication: " + mObject.adUnitId)
        }
    }

    override fun onAdLoaded() {
        super.onAdLoaded()

        if (mObject is AdView) {
            DLog.d("onAdLoaded: " + mObject.adUnitId)
            if (mObject.visibility == View.GONE) {
                mObject.visibility = View.VISIBLE
            }
        } else if (mObject is InterstitialAd) {
            if (DEBUG) {
                DLog.d("onAdLoaded: " + mObject.adUnitId)
            }
        }
    }

    override fun onAdClicked() {
        super.onAdClicked()

        if (DEBUG) {
            if (mObject is AdView) {
                DLog.d("onAdClicked: " + mObject.adUnitId)
            } else if (mObject is InterstitialAd) {
                DLog.d("onAdClicked: " + mObject.adUnitId)
            }
        }
    }

    override fun onAdImpression() {
        super.onAdImpression()

        if (DEBUG) {
            if (mObject is AdView) {
                DLog.d("onAdImpression: " + mObject.adUnitId)
            } else if (mObject is InterstitialAd) {
                DLog.d("onAdImpression: " + mObject.adUnitId)
            }
        }
    }

    companion object {
        private val DEBUG = BuildConfig.DEBUG
    }
}
