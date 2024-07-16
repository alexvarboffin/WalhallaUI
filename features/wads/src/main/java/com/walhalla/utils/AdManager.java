package com.walhalla.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.walhalla.library.R;

public class AdManager {

    public static final String KEY_ADMOB_AD = "a";
    public static final String KET_AF_AD = "f";
    public static final String REMOTE_AD = KEY_ADMOB_AD;

    public static int adCounter = 1;
    public static int adDisplayCounter = 10;
    private final String ADMOB_INTER_ID;

//    public static AlertDialog ProgressDialog;


    public AdManager(String a) {
        this.ADMOB_INTER_ID = a;
    }

    static void startActivity(Activity context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }


    public void initAD(Activity activity) {

//        if (REMOTE_AD.equals(KEY_ADMOB_AD)) {
//
//            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
//                @Override
//                public void onInitializationComplete(InitializationStatus initializationStatus) {
//                }
//            });
//        } else if (AdManager.REMOTE_AD.equals(REMOTE_AD)) {
//            AppLovinSdk.getInstance(activity).setMediationProvider("max");
//            AppLovinSdk.initializeSdk(activity, configuration -> {
//            });
//        }
    }

    public static void BannerAd(Activity activity, LinearLayout linearLayout, int color) {


    }


    public void LoadInterstitalAd(Activity activity) {

//        loadInterAd(activity);
//
//        maxInterstitialAd = new MaxInterstitialAd(activity.getResources().getString(R.string.max_interstitial), activity);
//        maxInterstitialAd.setListener(new MaxAdListener() {
//            @Override
//            public void onAdLoaded(MaxAd ad) {
//
//            }
//
//            @Override
//            public void onAdDisplayed(MaxAd ad) {
//
//            }
//
//            @Override
//            public void onAdHidden(MaxAd ad) {
//                maxInterstitialAd.loadAd();
//                startActivity(activity, maxIntent, maxRequstCode);
//            }
//
//            @Override
//            public void onAdClicked(MaxAd ad) {
//
//            }
//
//            @Override
//            public void onAdLoadFailed(String adUnitId, MaxError error) {
//                if (isNetworkConnected(activity)) {
//                    maxInterstitialAd.loadAd();
//                }
//                startActivity(activity, maxIntent, maxRequstCode);
////                    ProgressDialog.dismiss();
//            }
//
//            @Override
//            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//                if (isNetworkConnected(activity)) {
//                    maxInterstitialAd.loadAd();
//                }
//                startActivity(activity, maxIntent, maxRequstCode);
////                    ProgressDialog.dismiss();
//            }
//        });
//
//        if (isNetworkConnected(activity)) {
//            // Load the first ad
//            maxInterstitialAd.loadAd();
//        }

    }

    static InterstitialAd mInterstitialAd;


//    public void loadInterAd(Context context) {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(context, ADMOB_INTER_ID, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                mInterstitialAd = interstitialAd;
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                // Handle the error
//                mInterstitialAd = null;
////                ProgressDialog.dismiss();
//            startActivity((Activity) context, maxIntent, maxRequstCode);
//            }
//        });
//
//    }

    public static void showInterAd(final Activity context, final Intent intent, final int requestCode) {


    }

    public static void showMaxInterstitial(final Activity context, final Intent intent, final int requestCode) {
        startActivity(context, intent, requestCode);
    }
//


//    static boolean isNetworkConnected(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
//    }


    public void createNativeAdMAX(Activity context, RelativeLayout nativeAdContainer) {
        if (REMOTE_AD.equals(KEY_ADMOB_AD)) {
            String _id = context.getString(R.string.admob_native_id);
            AdLoader.Builder builder = new AdLoader.Builder(context, _id)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {

                            NativeAdView adView = (NativeAdView) context.getLayoutInflater()
                                    .inflate(R.layout.ad_lay, null);
                            // This method sets the text, images and the native ad, etc into the ad
                            // view.
                            populateNativeAdView(nativeAd, adView);
                            nativeAdContainer.removeAllViews();
                            nativeAdContainer.addView(adView);
                        }
                    });

            VideoOptions videoOptions =
                    new VideoOptions.Builder().setStartMuted(true).build();

            NativeAdOptions adOptions =
                    new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                }
                            })
                    .build();

            adLoader.loadAds(new AdRequest.Builder().build(), 10);

        } else if (REMOTE_AD.equals("f")) {
//            nativeAd = null;
//            MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(context.getResources().getString(R.string.max_native), context);
//            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
//                @Override
//                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
//                    // Clean up any pre-existing native ad to prevent memory leaks.
//                    if (nativeAd != null) {
//                        nativeAdLoader.destroy(nativeAd);
//                    }
//
//                    // Save ad for cleanup.
//                    nativeAd = ad;
//
//                    // Add ad view to view.
//                    nativeAdContainer.removeAllViews();
//                    nativeAdContainer.addView(nativeAdView);
//                }
//
//                @Override
//                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
//                    // We recommend retrying with exponentially higher delays up to a maximum delay
//                }
//
//                @Override
//                public void onNativeAdClicked(final MaxAd ad) {
//                    // Optional click callback
//                }
//            });
//            nativeAdLoader.loadAd();
        }

    }


    public static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {


    }

}
