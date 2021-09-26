package nl.walhalla.library;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.walhalla.library.BuildConfig;
//import static android.accounts.AccountManager.VISIBILITY_UNDEFINED;

public class InterAdListener extends com.google.android.gms.ads.AdListener {

    private static final String TAG = "@@@";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private final InterstitialAd interstitialAd;

    public InterAdListener(InterstitialAd adView) {
        this.interstitialAd = adView;
    }


    @Override
    public void onAdClosed() {
        super.onAdClosed();
        Log.d(TAG, "onAdClosed: " + interstitialAd.getAdUnitId());
    }


    @Override
    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        super.onAdFailedToLoad(loadAdError);
        if (DEBUG) {
            Log.d(TAG, String.format("Ad %s failed to load with error %d.",
                    interstitialAd.getAdUnitId(), loadAdError.getCode()));
        }
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
        Log.d(TAG, "onAdOpened: " + interstitialAd.getAdUnitId());
    }


//    @Override
//    public void onAdLeftApplication() {
//        super.onAdLeftApplication();
//        Log.d(TAG, "onAdLeftApplication: " + interstitialAd.getAdUnitId());
//    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        Log.d(TAG, "onAdLoaded: " + interstitialAd.getAdUnitId());
        //interstitialAd.show();
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
        Log.d(TAG, "onAdClicked: " + interstitialAd.getAdUnitId());
    }

    @Override
    public void onAdImpression() {
        super.onAdImpression();
        Log.d(TAG, "onAdImpression: " + interstitialAd.getAdUnitId());
    }
}
