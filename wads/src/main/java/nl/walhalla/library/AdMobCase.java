package nl.walhalla.library;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.UUID;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class AdMobCase {


    private static final String TAG = "@@@";

    public static RelativeLayout.LayoutParams attachToTop(int id) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                WRAP_CONTENT, WRAP_CONTENT
        );
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ABOVE, id);//not work if set ALIGN_PARENT_TOP
        return layoutParams;
    }


    public static RelativeLayout.LayoutParams attachToBottom(int id) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                WRAP_CONTENT, WRAP_CONTENT
        );
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.BELOW, id);//not work if set ALIGN_PARENT_TOP
        return layoutParams;
    }


    public static AdView createBanner(Context context, String banner_ad_unit_id) {
        int id = UUID.randomUUID().hashCode();
        AdView mAdView = new AdView(context);
        //mAdView.setVisibility(View.GONE);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(banner_ad_unit_id);
        mAdView.setId(id);
        return mAdView;
    }


    //#########################################################
    //admob
    //########################################################
    public static void interstitialBannerRequest(AdView mAdView) {
        // Start loading the ad in the background.
        mAdView.setAdListener(new AdListener(mAdView));
        mAdView.loadAd(buildAdRequest());
    }

    public static void interstitialBannerRequest(@NonNull InterstitialAd interstitialAd) {
        Log.i(TAG, "interstitialBannerRequest: ");
        // Start loading the ad in the background.
        interstitialAd.setAdListener(new AdListener(interstitialAd));
        interstitialAd.loadAd(AdMobCase.buildAdRequest());
    }

    public static AdRequest buildAdRequest() {
        AdRequest.Builder adRequest = new AdRequest.Builder();
        String[] arr = Const.testDevices();
        if (arr != null) {
            for (String device : arr) {
                adRequest.addTestDevice(device);
            }
        }
        return adRequest.build();
    }

    /**
     * show ads
     *
     *  if (config.interstitial_ad_unit_id != null) {
     // Create the InterstitialAd and set the adUnitId.
     InterstitialAd mInterstitialAd = new InterstitialAd(a);
     // Defined in res/values/strings.xml
     mInterstitialAd.setAdUnitId(config.interstitial_ad_unit_id);
     //prepare ads
     AdRequest adRequest = new AdRequest.Builder().build();
     mInterstitialAd.loadAd(adRequest);
     mInterstitialAdList.add(mInterstitialAd);
     }
     */
//    public void showInterstitial() {
//        // Show the ad if it's ready
//        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }
//    }
}
