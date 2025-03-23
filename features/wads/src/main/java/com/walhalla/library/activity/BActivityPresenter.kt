package com.walhalla.library.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.boilerplate.threading.MainThreadImpl;

import com.walhalla.domain.interactors.AdvertInteractor;
import com.walhalla.domain.interactors.impl.AdvertInteractorImpl;
import com.walhalla.domain.repository.AdvertRepository;
import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.wads.DLog;


/*
 *

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GDPR gdpr = new GDPR();
        gdpr.init(this);
    }

 *
 * */
public class BActivityPresenter {


    protected final long start_time = System.currentTimeMillis();
    private final AdvertAdmobRepository repository;
    private FrameLayout content;


    public BActivityPresenter(AdvertAdmobRepository repository) {
        this.repository = repository;
    }

    private final AdvertInteractor.Callback<View> callback = new AdvertInteractor.Callback<View>() {
        @Override
        public void onMessageRetrieved(int id, View message) {
            DLog.d(message.getClass().getName() + " --> " + message.hashCode());
            if (content != null) {
                DLog.d("@@@@@@@@@@" + content.getClass().getName());
                try {
                    //content.removeView(message);
                    if (message.getParent() != null) {
                        ((ViewGroup) message.getParent()).removeView(message);
                    }
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER;
                    message.setLayoutParams(params);


                    ViewTreeObserver vto = message.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("ObsoleteSdkInt")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < 16) {
                                message.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            //int width = message.getMeasuredWidth();
                            //int height = message.getMeasuredHeight();
                            //DLog.i("@@@@" + height + "x" + width);
                            //setSpaceForAd(height);
                        }
                    });
                    content.addView(message);

                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        }

        @Override
        public void onRetrievalFailed(String error) {
            DLog.d("---->" + error);
        }
    };


    public void setupAdAtBottom(FrameLayout content) {

        //FrameLayout content = findViewById(android.R.id.content);
        this.content = content;

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.BOTTOM;

//        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater()
//                .inflate(R.layout.ad_layout, null);
//        linearLayout.setLayoutParams(params);
//
//        // adding viewtreeobserver to get height of linearLayout layout , so that
//        // android.R.id.content will set margin of that height
//        ViewTreeObserver vto = linearLayout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @SuppressLint("ObsoleteSdkInt")
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT < 16) {
//                    linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//                int width = linearLayout.getMeasuredWidth();
//                int height = linearLayout.getMeasuredHeight();
//                //DLog.i("@@@@" + height + "x" + width);
//                setSpaceForAd(height);
//            }
//        });
//        addLayoutToContent(linearLayout);

        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), this.repository);
        //aa.attach(this);
        //DLog.d("---->" + aa.hashCode());
        interactor.selectView(content, callback);
    }


//    private void setSpaceForAd(int height) {
//        DLog.d("@@@@@@@@" + height);
////        FrameLayout content = findViewById(android.R.id.content);
////        if (content != null) {
////            View child0 = content.getChildAt(0);
////            //child0.setPadding(0, 0, 0, 50);
////
////            FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) child0.getLayoutParams();
////            //lp.bottomMargin = height;
////            child0.setLayoutParams(lp);
////        }
//    }

////    AdView adView = new AdView(this);
////    adView.setAdSize(AdSize.BANNER);
////    adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
//
//    private void addLayoutToContent(View ad) {
//        content.addView(ad);
//        AdView mAdView = ad.findViewById(R.id.adView);
//        //mAdView.setAdListener(new AdListener(mAdView));
//        mAdView.loadAd(new AdRequest.Builder().build());
//    }

}