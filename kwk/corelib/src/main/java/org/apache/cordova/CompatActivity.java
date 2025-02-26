package org.apache.cordova;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import androidx.appcompat.UWView;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.BackPressedUtil;
import org.apache.cordova.generated.P;
import org.apache.cordova.utils.Utils;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;
import com.walhalla.webview.ChromeView;

import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.cordova.domen.ScreenType;
import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.cordova.repository.AbstractDatasetRepository;
import org.apache.mvp.presenter.MainPresenter;
import org.apache.mvp.MainView;
import org.jetbrains.annotations.NotNull;


public abstract class CompatActivity extends AppCompatActivity
        implements ChromeView, MainView {

    protected final Gfg aaa;

    protected MainPresenter presenter;
    private boolean doubleBackToExitPressedOnce;

    //Views

    public SwipeRefreshLayout swipeRefreshLayout;
    protected UWView __mView;
    protected ViewGroup main;
    protected FrameLayout clazz1;
    protected ProgressBar pgb;

    protected Toolbar toolbar;
    protected boolean rotated0 = false;


    public CompatActivity() {
        aaa = GCfc();
    }

    protected ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;


    @Override
    public void hiDeRefreshLayout() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean rotated() {
        return rotated0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume(getIntent());
    }

    /**
     * активирует Web
     */
    @Override
    public void makeScreen(UIVisibleDataset screen) {
        if (screen.getScreenType() == ScreenType.WEB_VIEW) {
            if (!rotated0 && orientationWeb() != null && this.getRequestedOrientation() != orientationWeb()) {
                this.setRequestedOrientation(orientationWeb());
            }
            launch0(screen);
        } else if (screen.getScreenType() == ScreenType.WEB_RAW) {
            if (!rotated0 && orientationWeb() != null && this.getRequestedOrientation() != orientationWeb()) {
                this.setRequestedOrientation(orientationWeb());
            }
            launch0(screen);
        } else {
            if (!rotated0 && orientation404() != null && this.getRequestedOrientation() != orientation404()) {
                this.setRequestedOrientation(orientation404());
            }
        }


        Utils.hideKeyboard(this);
        boolean web =
                (screen.getScreenType() == ScreenType.WEB_VIEW)
                        || (screen.getScreenType() == ScreenType.WEB_RAW);

        clazz1.setVisibility((web) ? View.VISIBLE : View.GONE);
        main.setVisibility((web) ? View.GONE : View.VISIBLE);
        //mWebView.setVisibility((web) ? View.VISIBLE : View.GONE);

        if (web && aaa.isProgressbarEnabled()) {
            pgb.setVisibility(View.VISIBLE);
        } else {
            hideProgressBar();
        }
    }


    protected abstract Gfg GCfc();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler(Looper.getMainLooper());
        presenter = new MainPresenter(this, this, aaa, handler, makeTracker());
        setContentView(R.layout.activity_main);

        clazz1 = findViewById(R.id.viewer);
        main = findViewById(R.id.mainContainer);
        pgb = findViewById(R.id.progressBar1);
        if (!aaa.isProgressbarEnabled()) {
            hideProgressBar();
        }

        if (aaa.isToolbarEnabled()) {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
//            toolbar.setSubtitle(Util.getAppVersion(this));
            toolbar.setVisibility(View.VISIBLE);
        }


        if (!rotated()) {
            presenter.init0(this);
        }

        //Generate Dynamic Gui
        onViewCreated(clazz1, this);

//        Uri targetUrl =
//                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
//        if (targetUrl != null) {
//            Log.i(TAG, "App Link Target URL: " + targetUrl.toString());
//        }

//                if (rawUrl != null) {
//
//                    HttpPostRequest getRequest = new HttpPostRequest();
//                    try {
//                        String device_id = Util.phoneId(BaseActivity.this.getApplicationContext());
//
//                        //Replace deep to tracker
//                        URI uri = new URI(getString(R.string.app_url));
//
//                        String tracker =
//                                rawUrl.replace(getString(R.string.app_scheme), uri.getScheme())
//                                        .replace(getString(R.string.app_host), uri.getHost())
//                                        .replace("%26", "&")
//                                        .replace("%3D", "=")
//                                        + "&id=" + device_id;
//
////                        if (BuildConfig.DEBUG) {
////                            Log.i(TAG, "URL: " + rawUrl);
////                            Log.i(TAG, "TR: " + uri.getScheme() + "|" + uri.getHost());
////                            Log.i(TAG, "--->: " + tracker);
////
////                            //Only for Google Chrome test
////                            //
////
////                            Log.i(TAG, "CHROME: "
////                                    + ("intent://"
////                                    + getString(R.string.app_host)
////                                    + "/#Intent;scheme="
////                                    + getString(R.string.app_scheme) + ";package=" + getPackageName())
////                                    //        .replace(";",";")
////                                    + ";end"
////                            );
////                        }
//

//                        JSONObject parent = new JSONObject();
//                        try {
//                            parent.put("dl", rawUrl);
//                            parent.put("ref", storage.referer());
//                            getRequest.execute(parent).get();
//                        } catch (JSONException e) {
//                            DLog.handleException(e);
//                        }
//                    } catch (ExecutionException e) {
//                        DLog.handleException(e);
//                    } catch (InterruptedException e) {
//                        DLog.handleException(e);
//                    } catch (URISyntaxException e) {
//                        DLog.handleException(e);
//                    }
//                }

//        String device_id = Util.phoneId(MainActivity.getAppContext().getApplicationContext());
//        launch(webview_url + "?id=" + device_id);

//###        if (savedInstanceState != null) {
//###            return;
//###        }

//        if (checkUpdate()) {
//            if (toolbar != null) {
//                toolbar.post(() -> Module_U.checkUpdate(this));
//            }
//        }


    }

    protected abstract AbstractDatasetRepository makeTracker();


    @Override
    protected void onStop() {
        swipeRefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    protected void onViewCreated(ViewGroup view, Context context) {
        //mWebView = privacy.findViewById(R.id.web_view);
        //swipeRefreshLayout = privacy.findViewById(R.id.refresh);
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        __mView = new UWView(new ContextThemeWrapper(context, R.style.RadioButton2));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        swipeRefreshLayout.setLayoutParams(lp);
        __mView.setLayoutParams(lp);
        view.addView(swipeRefreshLayout);
        swipeRefreshLayout.addView(__mView);
        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                () -> {
                    swipeRefreshLayout.setEnabled(__mView.getScrollY() == 0);
                });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            __mView.reload();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //mWebView.setVisibility(View.INVISIBLE);
        //webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        presenter.a123(this, __mView);
    }

    public void onPageStarted() {
        //@@@
        if (aaa.isProgressbarEnabled()) {
            pgb.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPageFinished(String url) {
        presenter.saveFirstPageIfValid(url);
        String title = __mView.getTitle();
        if (webTitle() && !TextUtils.isEmpty(title) && toolbar != null) {
            if (title != null && title.startsWith(__mView.getUrl())) {
                toolbar.setSubtitle(title);
            }
        }
        hideProgressBar();

//        ShowOrHideWebViewInitialUse = "hide";
//        privacy.setVisibility(View.VISIBLE);

        __mView.setVisibility(View.VISIBLE);
        main = findViewById(R.id.mainContainer);

        if (main != null) {
            if (BuildConfig.DEBUG) {
                main.setBackgroundColor(Color.parseColor("#80000000"));
            } else {
                main.setVisibility(View.GONE);
            }
        }
    }

    private void hideProgressBar() {
        if (null != pgb) {
            pgb.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

//        if (fullLayout.isDrawerOpen(GravityCompat.START)) {
//            fullLayout.closeDrawer(GravityCompat.START);
//        } else {

        //DLog.d("@@@" + __mView.canGoBack()+"::"+__mView.getUrl());

        if (__mView.canGoBack()) {
            __mView.goBack();
        } else {

            //Log.d(TAG, "onBackPressed: " + getSupportFragmentManager().getBackStackEntryCount());


            //Pressed back => return to home screen
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(count > 0);
            }
            if (count > 0) {
                getSupportFragmentManager()
                        .popBackStack(getSupportFragmentManager()
                                        .getBackStackEntryAt(0).getId(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {//count == 0

//                Dialog
//                new AlertDialog.Builder(this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Leaving this App?")
//                        .setMessage("Are you sure you want to close this application?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
                //super.onBackPressed();


                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    try {
                        this.finish();
                    } catch (Exception ignored) {
                    }
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                BackPressedUtil.backPressedToast(this);
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1600);

            }


            /*
            //Next/Prev Navigation
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Leaving this App?")
                        .setMessage("Are you sure you want to close this application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else
            {
            super.onBackPressed();
            }
            */

        }
    }


    public void launch0(UIVisibleDataset aaa) {
        if (aaa.getScreenType() == ScreenType.WEB_VIEW) {
            //__mView.clearHistory();
            __mView.post(() -> __mView.loadUrl(aaa.getUrl()));
        } else if (aaa.getScreenType()  == ScreenType.WEB_RAW) {
            DLog.d("@@WEB_RAW@@" + aaa.getUrl());
            __mView.loadDataWithBaseURL("https://slot", aaa.getUrl(), "text/html", "utf-8", null);
        }
    }

//    @SuppressLint("SourceLockedOrientationActivity")
//    @Override
//    public void m404() {
//        makeScreen(new UIVisibleDataset(ScreenType.WEB_VIEW, NONENONE));
//        if (toolbar != null) {
//            toolbar.setSubtitle("");
//        }
//    }

    @Override
    public void successResult(String var0) {
        //DLog.d("@@@" + var0 + "\n");
    }

    protected void actionRefresh() {
        if (!TextUtils.isEmpty(__mView.getUrl())) {
            __mView.reload();
            //getContent(url);
            Snackbar.make(getWindow().getDecorView(), /*url*/"Data updated.", Snackbar.LENGTH_SHORT)
                    .setAction(android.R.string.ok, null).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putBoolean(P.KEY_ROTATED, rotated0);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rotated0 = savedInstanceState.getBoolean(P.KEY_ROTATED, false);
    }


    public void replaceFragment0(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
