package com.walhalla.domain.repository.from_internet;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import com.google.android.material.appbar.AppBarLayout;
import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.threader.BuildConfig;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import com.walhalla.domain.repository.AdvertRepository;
import com.walhalla.library.AdMobCase;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by combo on 18.07.2017.
 * <p>
 * <p>
 * demo ads => "ca-app-pub-3940256099942544/6300978111"
 */


public class AdvertAdmobRepository
        implements AdvertRepository, LifecycleObserver {


    private static final boolean DEBUG = BuildConfig.DEBUG;
    private final Object lock = new Object();

    private long startTime = System.currentTimeMillis();


    private final AdvertConfig config;

    /**
     * Getter & Setter
     */
    public HashMap<Integer, AdView> getHashMap() {
        return mAdViewHashMap;
    }

    public void setHashMap(HashMap<Integer, AdView> hashMap) {
        mAdViewHashMap = hashMap;
    }

    /**
     * ads
     */
    private HashMap<Integer, AdView> mAdViewHashMap;
    //private HashMap<Integer, RewardedVideoAd> mRewardedVideoAdHashMap;

    private List<InterstitialAd> mInterstitialAdList;


    private static final String TAG = "@@@@";

    private volatile static int position = -1;

    private static AdvertAdmobRepository INSTANCE;

    public static AdvertAdmobRepository getInstance(AdvertConfig config) {
        if (INSTANCE == null) {
            INSTANCE = new AdvertAdmobRepository(config);
        }
        return INSTANCE;
    }


//    public ViewAdmobRepository(Context context) {
//        this.mContext = context;
//    }

    @SuppressLint("UseSparseArrays")
    private AdvertAdmobRepository(@NonNull AdvertConfig config) {
        this.config = config;
        this.mAdViewHashMap = new HashMap<>();
        if (DEBUG) {
            Log.d(TAG, "AdMobCase: " + this.hashCode());
        }
    }


//    @Override
//    public void initialize(Context context) {
//        MobileAds.initialize(context, config.application_id());
//        position = -1;
//    }


    public void initialize(@NonNull Activity context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                //config.application_id()
            }
        });
        position = -1;
    }


    //Getter
    @Override
    public AdView getNewAdsBanner(ViewGroup viewGroup) {
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
        AdView adView;
        int key = viewGroup.getId();

        synchronized (lock) {
            position++;
            Log.i(TAG, "[+]: position: " + position);
            if (mAdViewHashMap.isEmpty() || mAdViewHashMap.get(key) == null) {

                //Create new banner
                if (position > config.banner_ad_unit_id.size()) {
                    position = 0;
                }
                int mapKey = config.banner_ad_unit_id.keyAt(position);
                String banner_ad_unit_id = config.banner_ad_unit_id.get(mapKey);
                adView = AdMobCase.createBanner(viewGroup.getContext(), banner_ad_unit_id);
                mAdViewHashMap.put(key, adView);

            } else {
                adView = mAdViewHashMap.get(key);
            }
        }
        return adView;
    }


    //Fail
    private void attach(ViewGroup viewGroup, int gravity) {
        Handler mainHandler = new Handler(viewGroup.getContext().getMainLooper());
        ThreadExecutor threadExecutor = new ThreadExecutor();

        Future<?> future = threadExecutor.execute(() -> {

            try {

                int count = viewGroup.getChildCount();

                AdView mAdView = getNewAdsBanner(viewGroup);


                if (viewGroup instanceof RelativeLayout) {

                    RelativeLayout relativeLayout = (RelativeLayout) viewGroup;

                    //Resize block
                    ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
                    params.height = params.height + 50;


                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            WRAP_CONTENT, WRAP_CONTENT
                    );
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    if (count > 0) {


                        //RelativeLayout.LayoutParams child_lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);


                        switch (gravity) {

                            case Gravity.TOP:
                                View child = relativeLayout.getChildAt(0);
                                RelativeLayout.LayoutParams child_lp = (RelativeLayout.LayoutParams) child.getLayoutParams();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    child_lp.removeRule(RelativeLayout.ALIGN_PARENT_TOP);//Remove top
                                } else {
                                    child_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);//Remove top
                                }
                                layoutParams = AdMobCase.attachToTop(child.getId());
                                mAdView.setLayoutParams(layoutParams);
                                break;

                            case Gravity.BOTTOM:
                                //default:
                                child = relativeLayout.getChildAt(count - 1);
                                //child_lp.addRule(RelativeLayout.ABOVE, mAdView.getId());
                                child_lp = (RelativeLayout.LayoutParams) child.getLayoutParams();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    child_lp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//Remove bottom
                                } else {
                                    child_lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);//Remove bottom
                                }
                                layoutParams = AdMobCase.attachToBottom(child.getId());
                                mAdView.setLayoutParams(layoutParams);
                                //viewGroup.addView(button, count/*lp*/);//ok

                                break;
                        }


                    } else {
                        switch (gravity) {

                            case Gravity.TOP:
                                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                break;

                            case Gravity.BOTTOM:
                                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                break;
                        }
                    }

                    mAdView.setLayoutParams(layoutParams);
                    relativeLayout.addView(mAdView, count);

                } else if (viewGroup instanceof LinearLayout) {

                    LinearLayout linearLayout = (LinearLayout) viewGroup;
                    linearLayout.addView(mAdView);

                } else if (viewGroup instanceof CoordinatorLayout) {


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


                    CoordinatorLayout.LayoutParams layoutParams =
                            new CoordinatorLayout.LayoutParams(WRAP_CONTENT,
                                    WRAP_CONTENT);
//                    (CoordinatorLayout.LayoutParams) viewGroup.getLayoutParams();

                    if (gravity == Gravity.TOP) {
                        layoutParams.anchorGravity = Gravity.BOTTOM;
//                            layoutParams.setAnchorId(R.id.app_bar);
                        layoutParams.setBehavior(
                                new AppBarLayout.ScrollingViewBehavior(viewGroup.getContext(), null));
                    } else {
                        //Bottom
                        layoutParams.gravity = gravity | Gravity.CENTER_HORIZONTAL;
                    }

//            layout.setLayoutParams(layoutParams);


                    viewGroup.addView(mAdView, layoutParams); //  <==========  ERROR IN THIS LINE DURING 2ND RUN


                    Log.i(TAG, "@@@: " + mAdView.getAdUnitId() + mAdView.hashCode());


//            layout.addView(mAdView);
                    //viewGroup.addView(banner, params);
                } else if (viewGroup instanceof AdView) {
                    getHashMap().put(viewGroup.getId(), (AdView) viewGroup);
                } else if (viewGroup instanceof DrawerLayout) {
                    attach(((ViewGroup) viewGroup.getChildAt(0)), gravity);
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

                    mainHandler.post(() -> {
                        if (mAdView.getParent() != null) {
                            ((ViewGroup) mAdView.getParent()).removeView(mAdView); // <- fix
                        }
                        viewGroup.addView(mAdView);
                    });
                }


//                    threadExecutor.postOnUiThread(() -> AdMobCase.interstitialBannerRequest(mAdView));
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AdMobCase.interstitialBannerRequest(mAdView);
                    }
                });

            } catch (Exception e) {
                Log.i(TAG, "run-exception: " + e.getMessage());

            }


        });
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    //Other attach
    public void attach(Activity activity, int gravity) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity
                .findViewById(android.R.id.content)).getChildAt(0);//#Empty if not set content
        attach(viewGroup, gravity);
    }

    public void attach(Activity activity) {
        attach(activity, Gravity.BOTTOM);

    }

    public void admobBannerCall(ViewGroup viewGroup) {
        attach(viewGroup, Gravity.BOTTOM);
    }


    //=======================================================================================
    //
    //
    //=======================================================================================
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void oncreate() {

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void pause() {

        for (AdView adView : getHashMap().values()) {
            if (adView != null) {
                Log.d(TAG, "pause: " + adView.getAdUnitId() + " " + adView.hashCode());
                adView.pause();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void resume() {
        for (AdView adView : getHashMap().values()) {
            if (adView != null) {
                if (DEBUG) {
                    Log.d(TAG, "resume: " + adView.getAdUnitId() + " " + adView.hashCode());
                }
                adView.resume();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void destroy() {
        Log.d(TAG, "destroy: ");
//        for (AdView adView : getHashMap().values()) {
//            if (adView != null) {
//                adView.destroy();
//            }
//        }
    }
}
