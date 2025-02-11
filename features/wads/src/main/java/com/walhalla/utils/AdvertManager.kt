package com.walhalla.utils

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.walhalla.library.databinding.AdLayBinding

class AdvertManager  //    public static AlertDialog ProgressDialog;
private constructor() {
    var REMOTE_AD: AdsIds? = null

    fun init(m: AdmobAdsIds?) {
        this.REMOTE_AD = m
    }

    fun initAD(activity: Activity?) {
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

    fun LoadInterstitalAd(activity: Activity?) {
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

    //
    //    static boolean isNetworkConnected(Context context) {
    //        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    //
    //        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    //    }
    fun createNativeAdMAX(context: Activity, nativeAdContainer: RelativeLayout) {
        if (REMOTE_AD == null) {
            return
        }

        if (REMOTE_AD is AdmobAdsIds) {
            val m = REMOTE_AD as AdmobAdsIds
            if (!TextUtils.isEmpty(m.admob_native_id)) {
                val builder = AdLoader.Builder(
                    context,
                    m.admob_native_id!!
                )
                    .forNativeAd { nativeAd ->
                        val binding = AdLayBinding.inflate(
                            LayoutInflater.from(context)
                        )
                        // This method sets the text, images and the native ad, etc into the ad
                        // view.
                        populateNativeAdView(nativeAd, binding)
                        nativeAdContainer.removeAllViews()
                        nativeAdContainer.addView(binding.root)
                    }

                val videoOptions =
                    VideoOptions.Builder().setStartMuted(true).build()

                val adOptions =
                    NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

                builder.withNativeAdOptions(adOptions)

                val adLoader = builder.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    }
                })
                    .build()

                adLoader.loadAds(AdRequest.Builder().build(), 10)
            }
        } else if (REMOTE_AD is MaxAdsIds) {
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


    companion object {
        var adCounter: Int = 1
        var adDisplayCounter: Int = 10


        private var instance: AdvertManager? = null

        @Synchronized
        fun getInstance(): AdvertManager {
            if (instance == null) {
                instance = AdvertManager()
            }
            return instance!!
        }

        fun startActivity(context: Activity, intent: Intent?, requestCode: Int) {
            if (intent != null) {
                context.startActivityForResult(intent, requestCode)
            }
        }


        fun BannerAd(activity: Activity?, linearLayout: LinearLayout?, color: Int) {
        }


        var mInterstitialAd: InterstitialAd? = null


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
        fun showMaxInterstitial(context: Activity, intent: Intent?, requestCode: Int) {
            startActivity(context, intent, requestCode)
        }


        fun populateNativeAdView(nativeAd: NativeAd, binding: AdLayBinding) {
            // Set other ad assets.

            val my = binding.root
            my.mediaView = binding.adMedia
            my.headlineView = binding.adHeadline
            my.bodyView = binding.adBody
            my.callToActionView = binding.adCallToAction
            my.iconView = binding.adIcon
            my.priceView = binding.adPrice
            my.starRatingView = binding.adStars
            my.storeView = binding.adStore
            my.advertiserView = binding.adAdvertiser

            // The headline is guaranteed to be in every UnifiedNativeAd.
            (my.headlineView as TextView).text = nativeAd.headline


            if (nativeAd.callToAction == null) {
                my.callToActionView?.visibility = View.INVISIBLE
            } else {
                my.callToActionView?.visibility = View.VISIBLE
                (my.callToActionView as Button).text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                my.iconView?.visibility = View.GONE
            } else {
                (my.iconView as ImageView).setImageDrawable(
                    nativeAd.icon!!.drawable
                )
                my.iconView?.visibility = View.VISIBLE
            }

            if (nativeAd.price == null) {
                my.priceView?.visibility = View.INVISIBLE
            } else {
                my.priceView?.visibility = View.VISIBLE
                (my.priceView as TextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                my.storeView?.visibility = View.INVISIBLE
            } else {
                my.storeView?.visibility = View.VISIBLE
                (my.storeView as TextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                my.starRatingView?.visibility = View.INVISIBLE
            } else {
                (my.starRatingView as RatingBar).rating =
                    nativeAd.starRating!!.toFloat()
                my.starRatingView?.visibility = View.VISIBLE
            }
            my.setNativeAd(nativeAd)
        }
    }
}
