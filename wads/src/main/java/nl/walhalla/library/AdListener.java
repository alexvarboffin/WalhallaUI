package nl.walhalla.library;

import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.walhalla.library.BuildConfig;

import static android.accounts.AccountManager.PACKAGE_NAME_KEY_LEGACY_VISIBLE;


public class AdListener extends com.google.android.gms.ads.AdListener {

    private static final String TAG = "@@@";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private final Object mObject;

    //Constructor #1
    public AdListener(Object o) {
        super();
        this.mObject = o;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();

        if (DEBUG) {
            if (mObject instanceof AdView) {
                Log.d(TAG, "onAdClosed: " + ((AdView) mObject).getAdUnitId());
            } else if (mObject instanceof InterstitialAd) {
                InterstitialAd interstitialAd = (InterstitialAd) mObject;
                Log.d(TAG, "onAdClosed: " + interstitialAd.getAdUnitId());

                // Load the next interstitial.
                interstitialAd.loadAd(AdMobCase.buildAdRequest());
            }
        }
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
        if (DEBUG) {

            String errorReason = "";
            switch(errorCode) {
                case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                    errorReason = "Internal error";
                    break;
                case AdRequest.ERROR_CODE_INVALID_REQUEST:
                    errorReason = "Invalid request";
                    break;
                case AdRequest.ERROR_CODE_NETWORK_ERROR:
                    errorReason = "Network Error";
                    break;
                    /*
                    * The ad request was successful, but no ad was returned due to lack of ad inventory.
                    * */
                case AdRequest.ERROR_CODE_NO_FILL:
                    errorReason = "No fill";

//                PACKAGE_NAME_KEY_LEGACY_VISIBLE}, //#{@link #PACKAGE_NAME_KEY_LEGACY_NOT_VISIBLE
//                case VISIBILITY_UNDEFINED:
//                    errorReason = "onAdFailedToLoad: VISIBILITY_UNDEFINED";
                    break;
            }

            if (mObject instanceof AdView) {
                Log.d(TAG, String.format("Ad %s failed to load with error %s.", ((AdView) mObject).getAdUnitId(), errorReason));

                if (errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                    if (((AdView) mObject).getVisibility() == View.VISIBLE) {
                        ((AdView) mObject).setVisibility(View.GONE);
                    }
                }
            } else if (mObject instanceof InterstitialAd) {
                Log.d(TAG, String.format("Ad %s failed to load with error %s.", ((InterstitialAd) mObject).getAdUnitId(), errorReason));
            }
        }
    }


    @Override
    public void onAdOpened() {
        super.onAdOpened();
        if (mObject instanceof AdView) {
            Log.d(TAG, "onAdOpened: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            Log.d(TAG, "onAdOpened: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }


    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
        if (mObject instanceof AdView) {
            Log.d(TAG, "onAdLeftApplication: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            Log.d(TAG, "onAdLeftApplication: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();

        if (mObject instanceof AdView) {
            Log.d(TAG, "onAdLoaded: " + ((AdView) mObject).getAdUnitId());
            if (((AdView) mObject).getVisibility() == View.GONE) {
                ((AdView) mObject).setVisibility(View.VISIBLE);
            }
        } else if (mObject instanceof InterstitialAd) {
            Log.d(TAG, "onAdLoaded: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();

        if (mObject instanceof AdView) {
            Log.d(TAG, "onAdClicked: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            Log.d(TAG, "onAdClicked: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }

    @Override
    public void onAdImpression() {
        super.onAdImpression();

        if (mObject instanceof AdView) {
            Log.d(TAG, "onAdImpression: " + ((AdView) mObject).getAdUnitId());
        } else if (mObject instanceof InterstitialAd) {
            Log.d(TAG, "onAdImpression: " + ((InterstitialAd) mObject).getAdUnitId());
        }
    }
}
