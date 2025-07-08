package com.walhalla.domain.repository.from_internet

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.appbar.AppBarLayout
import com.walhalla.domain.repository.AdvertRepository
import com.walhalla.library.AdMobCase.attachToBottom
import com.walhalla.library.AdMobCase.attachToTop
import com.walhalla.library.AdMobCase.createBanner
import com.walhalla.library.AdMobCase.interstitialBannerRequest
import com.walhalla.library.BuildConfig
import java.util.concurrent.ExecutionException
import kotlin.concurrent.Volatile

/**
 * Created by combo on 18.07.2017.
 *
 *
 *
 *
 * demo ads => "ca-app-pub-3940256099942544/6300978111"
 */
class AdvertAdmobRepository
@SuppressLint("UseSparseArrays") private constructor(private val config: AdvertConfig) :
    AdvertRepository, LifecycleObserver {
    private val lock = Any()

    private val startTime = System.currentTimeMillis()


    /**
     * Getter & Setter
     */
    /**
     * ads
     */
    var hashMap: HashMap<Int, AdView?> = HashMap()

    //private HashMap<Integer, RewardedVideoAd> mRewardedVideoAdHashMap;
    private val mInterstitialAdList: List<InterstitialAd>? = null


    //    public ViewAdmobRepository(Context context) {
    //        this.mContext = context;
    //    }
    init {
        if (DEBUG) {
            Log.d(TAG, "AdMobCase: " + this.hashCode())
        }
    }


    //    @Override
    //    public void initialize(Context context) {
    //        MobileAds.initialize(context, config.application_id());
    //        position = -1;
    //    }
    fun initialize(context: Activity) {
//        MobileAds.initialize(context, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
//                //config.application_id()
//            }
//        });
        position = -1
    }


    //Getter
    override fun getNewAdsBanner(viewGroup: ViewGroup): AdView {
//        Log.i(TAG, "run: " + Thread.currentThread().getName()
//                + "" + "@" + viewGroup.getClass().getSimpleName());
//
//        long seconds = ((System.currentTimeMillis() - startTime)) / 1000;
//        String msg = "update success: " + seconds; // let's be friendly

//        attach(viewGroup, Gravity.TOP);

//        // давайте симулируем некоторые сетевые/БД лаги
//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Button message = new Button(viewGroup.getContext());
//        message.setText("000000000000000000000000000000000");

        var adView: AdView?
        val key = viewGroup.id

        synchronized(lock) {
            position++
            Log.i(TAG, "[+]: position: " + position)
            if (hashMap.isEmpty() || hashMap[key] == null) {
                //Create new banner

                if (position > config.banner_ad_unit_id.size()) {
                    position = 0
                }
                val mapKey = config.banner_ad_unit_id.keyAt(position)
                val banner_ad_unit_id = config.banner_ad_unit_id[mapKey]
                adView = createBanner(viewGroup.context, banner_ad_unit_id)
                hashMap.put(key, adView)
            } else {
                adView = hashMap[key]
            }
        }
        return adView!!
    }


    //Fail
    private fun attach(viewGroup: ViewGroup, gravity: Int) {
        val mainHandler = Handler(viewGroup.context.mainLooper)
        val threadExecutor = com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor()

        val future = threadExecutor.execute {
            try {
                val count = viewGroup.childCount

                val mAdView = getNewAdsBanner(viewGroup)


                if (viewGroup is RelativeLayout) {
                    val relativeLayout = viewGroup

                    //Resize block
                    val params = relativeLayout.layoutParams
                    params.height = params.height + 50


                    var layoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                    if (count > 0) {
                        //RelativeLayout.LayoutParams child_lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);


                        when (gravity) {
                            Gravity.TOP -> {
                                val child = relativeLayout.getChildAt(0)
                                val child_lp =
                                    child.layoutParams as RelativeLayout.LayoutParams
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    child_lp.removeRule(RelativeLayout.ALIGN_PARENT_TOP) //Remove top
                                } else {
                                    child_lp.addRule(
                                        RelativeLayout.ALIGN_PARENT_TOP,
                                        0
                                    ) //Remove top
                                }
                                layoutParams = attachToTop(child.id)
                                mAdView.layoutParams = layoutParams
                            }

                            Gravity.BOTTOM -> {
                                //default:
                                val child = relativeLayout.getChildAt(count - 1)
                                //child_lp.addRule(RelativeLayout.ABOVE, mAdView.getId());
                                val child_lp = child.getLayoutParams() as RelativeLayout.LayoutParams
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    child_lp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM) //Remove bottom
                                } else {
                                    child_lp.addRule(
                                        RelativeLayout.ALIGN_PARENT_BOTTOM,
                                        0
                                    ) //Remove bottom
                                }
                                layoutParams = attachToBottom(child.getId())
                                mAdView.layoutParams = layoutParams
                            }
                        }
                    } else {
                        when (gravity) {
                            Gravity.TOP -> layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                            Gravity.BOTTOM -> layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        }
                    }

                    mAdView.layoutParams = layoutParams
                    relativeLayout.addView(mAdView, count)
                } else if (viewGroup is LinearLayout) {
                    viewGroup.addView(mAdView)
                } else if (viewGroup is CoordinatorLayout) {
                    //Attach in linerlayout
//            LinearLayout linearLayout = new LinearLayout(viewGroup.getContext());
//            linearLayout.setOrientation(LinearLayout.VERTICAL);
//            linearLayout.setLayoutParams(..CoordinatorLayout.LayoutParams..);
//            linearLayout.addView(ads);


//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) viewGroup.getLayoutParams();
//            //params.setAnchorId(R.id.app_bar_layout);
//            params.anchorGravity = Gravity.BOTTOM; //we will anchor to the bottom line of the appbar
//            params.gravity = Gravity.BOTTOM; //we want to be BELOW that line
//            viewGroup.setLayoutParams(params);

                    //MobileAds.initialize(context, context.getString(R.string.application_id));


                    val layoutParams =
                        CoordinatorLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                    //                    (CoordinatorLayout.LayoutParams) viewGroup.getLayoutParams();
                    if (gravity == Gravity.TOP) {
                        layoutParams.anchorGravity = Gravity.BOTTOM
                        //                            layoutParams.setAnchorId(R.id.app_bar);
                        layoutParams.behavior =
                            AppBarLayout.ScrollingViewBehavior(viewGroup.getContext(), null)
                    } else {
                        //Bottom
                        layoutParams.gravity = gravity or Gravity.CENTER_HORIZONTAL
                    }


                    //            layout.setLayoutParams(layoutParams);
                    viewGroup.addView(
                        mAdView,
                        layoutParams
                    ) //  <==========  ERROR IN THIS LINE DURING 2ND RUN


                    Log.i(
                        TAG,
                        "@@@: " + mAdView.adUnitId + mAdView.hashCode()
                    )


                    //            layout.addView(mAdView);
                    //viewGroup.addView(banner, params);
                } else if (viewGroup is AdView) {
                    hashMap[viewGroup.getId()] = viewGroup
                } else if (viewGroup is DrawerLayout) {
                    attach((viewGroup.getChildAt(0) as ViewGroup), gravity)
                } else {
                    //frame_layout
                    //lp
                    //        viewGroup.setBackgroundColor(Color.BLUE);


//                        threadExecutor.postOnUiThread(() -> {
//                            if (mAdView.getParent() != null) {
//                                ((ViewGroup) mAdView.getParent()).removeView(mAdView); // <- fix
//                            }
//                            viewGroup.addView(mAdView);
//
//                        });


                    mainHandler.post {
                        if (mAdView.parent != null) {
                            (mAdView.parent as ViewGroup).removeView(mAdView) // <- fix
                        }
                        viewGroup.addView(mAdView)
                    }
                }


                //                    threadExecutor.postOnUiThread(() -> AdMobCase.interstitialBannerRequest(mAdView));
                mainHandler.post { interstitialBannerRequest(mAdView) }
            } catch (e: Exception) {
                Log.i(
                    TAG,
                    "run-exception: " + e.message
                )
            }
        }
        try {
            future?.get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
    }


    //Other attach
    @JvmOverloads
    fun attach(activity: Activity, gravity: Int = Gravity.BOTTOM) {
        val viewGroup = (activity
            .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup //#Empty if not set content
        attach(viewGroup, gravity)
    }

    fun admobBannerCall(viewGroup: ViewGroup) {
        attach(viewGroup, Gravity.BOTTOM)
    }


    //=======================================================================================
    //
    //
    //=======================================================================================
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun oncreate() {
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun pause() {
        for (adView in hashMap.values) {
            if (adView != null) {
                Log.d(TAG, "pause: " + adView.adUnitId + " " + adView.hashCode())
                adView.pause()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun resume() {
        for (adView in hashMap.values) {
            if (adView != null) {
                if (DEBUG) {
                    Log.d(TAG, "resume: " + adView.adUnitId + " " + adView.hashCode())
                }
                adView.resume()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        Log.d(TAG, "destroy: ")
//        for (AdView adView : getHashMap().values()) {
//            if (adView != null) {
//                adView.destroy();
//            }
//        }
    }

    companion object {
        private val DEBUG = BuildConfig.DEBUG
        private const val TAG = "@@@@"

        @Volatile
        private var position = -1

        private var INSTANCE: AdvertAdmobRepository? = null

        @JvmStatic
        fun getInstance(config: AdvertConfig): AdvertAdmobRepository {
            if (INSTANCE == null) {
                INSTANCE = AdvertAdmobRepository(config)
            }
            return INSTANCE!!
        }
    }
}
