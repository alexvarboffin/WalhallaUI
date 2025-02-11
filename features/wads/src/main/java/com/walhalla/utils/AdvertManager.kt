package com.walhalla.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.walhalla.library.R;
import com.walhalla.library.databinding.AdLayBinding;

public class AdvertManager {


    AdsIds REMOTE_AD;

    public void init(AdmobAdsIds m) {
        this.REMOTE_AD = m;
    }

    public static int adCounter = 1;
    public static int adDisplayCounter = 10;


//    public static AlertDialog ProgressDialog;


    private AdvertManager() {
    }

    static AdvertManager instance;

    public static synchronized AdvertManager getInstance() {
        if (instance == null) {
            instance = new AdvertManager();
        }
        return instance;
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
//        InterstitialAd.load(context, REMOTE_AD.ADMOB_INTER_ID, adRequest, new InterstitialAdLoadCallback() {
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
//    }

//    public void showInterAd(final Activity context, final Intent intent, final int requestCode) {
//
//        if (mInterstitialAd != null) {
//
//            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    loadInterAd(context);
//                    startActivity(context, maxIntent, maxRequstCode);
//                }
//
//                @Override
//                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
////                        ProgressDialog.dismiss();
//                }
//
//
//                @Override
//                public void onAdShowedFullScreenContent() {
//                    mInterstitialAd = null;
//
//                }
//            });
//
//            maxIntent = intent;
//            maxRequstCode = requestCode;
//
//            if (isNetworkConnected(context)) {
//                if (adCounter == adDisplayCounter && mInterstitialAd != null) {
//                    adCounter = 1;
////                        Ad_Popup(context);
//
//                    mInterstitialAd.show((Activity) context);
////                            ProgressDialog.dismiss();
//
//                } else {
//                    if (adCounter == adDisplayCounter) {
//                        adCounter = 1;
//                    }
//                    startActivity(context, intent, requestCode);
//                }
//            } else {
//                startActivity(context, intent, requestCode);
//            }
//
//        }
//    }

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

        if (REMOTE_AD == null) {
            return;
        }

        if (REMOTE_AD instanceof AdmobAdsIds) {
            AdmobAdsIds m = (AdmobAdsIds) REMOTE_AD;
            if (!TextUtils.isEmpty(m.admob_native_id)) {
                AdLoader.Builder builder = new AdLoader.Builder(context, m.admob_native_id)
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {

                                AdLayBinding binding = AdLayBinding.inflate(LayoutInflater.from(context));
                                // This method sets the text, images and the native ad, etc into the ad
                                // view.
                                populateNativeAdView(nativeAd, binding);
                                nativeAdContainer.removeAllViews();
                                nativeAdContainer.addView(binding.getRoot());
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
            }


        } else if (REMOTE_AD instanceof MaxAdsIds) {
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


    public static void populateNativeAdView(NativeAd nativeAd, AdLayBinding binding) {

        // Set other ad assets.
        NativeAdView my = binding.getRoot();
        my.setMediaView(binding.adMedia);
        my.setHeadlineView(binding.adHeadline);
        my.setBodyView(binding.adBody);
        my.setCallToActionView(binding.adCallToAction);
        my.setIconView(binding.adIcon);
        my.setPriceView(binding.adPrice);
        my.setStarRatingView(binding.adStars);
        my.setStoreView(binding.adStore);
        my.setAdvertiserView(binding.adAdvertiser);

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) my.getHeadlineView()).setText(nativeAd.getHeadline());


        if (nativeAd.getCallToAction() == null) {
           my.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            my.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) my.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            my.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) my.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            my.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            my.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            my.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) my.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            my.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            my.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) my.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            my.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) my.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            my.getStarRatingView().setVisibility(View.VISIBLE);
        }
        my.setNativeAd(nativeAd);
    }

}
