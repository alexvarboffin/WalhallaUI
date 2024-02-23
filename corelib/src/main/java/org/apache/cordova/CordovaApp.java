package org.apache.cordova;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.android.lib.navigation.Navigation;

import org.apache.BackPressedUtil;
import org.apache.P;
import org.apache.Utils;

import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.cordova.fragment.WebFragment;
import org.apache.cordova.repository.DatasetRepository;
import com.walhalla.ui.DLog;
import org.apache.mvp.presenter.MainPresenter;
import org.apache.mvp.MainView;
import org.jetbrains.annotations.NotNull;


public abstract class CordovaApp extends AppCompatActivity
        implements MainView, WebFragment.Lecallback {


    protected MainPresenter presenter;
    protected final GConfig aaa;

    private boolean doubleBackToExitPressedOnce;
    protected Navigation mNav;

    //Views

    protected Toolbar toolbar;
    protected boolean rotated = false;

    private DatasetRepository repo;
    private String __url__;
    private TPreferences pref;

    public CordovaApp() {
        aaa = GCfc();
    }

    protected abstract GConfig GCfc();

    @Override
    public void setActionBarTitle(String title) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!rotated) {
            presenter.onResume(getIntent());
        }
    }

    /**
     * активирует Web
     */
    @Override
    public void makeScreen(UIVisibleDataset screen) {
        Utils.hideKeyboard(this);
        if (screen.getScreenType() != ScreenType.WEB_VIEW) {
            if (!rotated && orientation404() != null && this.getRequestedOrientation() != orientation404()) {
                this.setRequestedOrientation(orientation404());
                rotated = true;
            }
        } else {
            if (!rotated && orientationWeb() != null && this.getRequestedOrientation() != orientationWeb()) {
                this.setRequestedOrientation(orientationWeb());
                rotated = true;
            }
            launchER(screen.getUrl());
        }

//        boolean web = screengetScreenType() == ScreenType.WEB_VIEW;
//        if (web) {
//            launchER(screen.getUrl());
//        } else {
//            //replaceFragment();
//            DLog.d("@@" + screen);
//        }

        //mWebView.setVisibility((web) ? View.VISIBLE : View.GONE);
//        if (web && PROGRESSBAR_ENABLED) {
//            pgb.setVisibility(View.VISIBLE);
//        } else {
//            hideProgressBar();
//        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNav = new Navigation(this);

        Handler handler = new Handler(Looper.getMainLooper());
        presenter = new MainPresenter(
                this, this, aaa, handler, makeTracker());

        pref = TPreferences.getInstance(this);
        setContentView(R.layout.activity_holder);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (aaa.isTOOLBAR_ENABLED()) {
//            toolbar.setSubtitle(Util.getAppVersion(this));
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (!rotated()) {
            //DLog.d("@@@@@@@@@@@@");
            presenter.init0(this);
        }

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
//                        LocalStorage storage = LocalStorage.getInstance(BaseActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


//    @Override
//    public void onBackPressed() {
////        final List<Fragment> aa = getSupportFragmentManager().getFragments();
////        if (aa.size() > 0) {
////            for (Fragment fragment : aa) {
////                if (fragment instanceof WebFragment) {
////                    WebFragment rr = (WebFragment) fragment;
////                    if (rr.mWebView.canGoBack()) {
////                        rr.mWebView.goBack();
////                        DLog.d("@@@@@");
////
////                        if (!__url__.equals(rr.mWebView.getUrl())) {
////                            //@@@ onMenuSelected(0);
////                        }
////
////                    } else {
////                        DLog.d("@@@over1" + rr.mWebView.getUrl());
////                    }
////                    if (__url__.equals(rr.mWebView.getUrl())) {
////                        over();
////                    }
////                } else {
////                    DLog.d("@@@over 2  " + fragment.getClass().getSimpleName());
////                    over();
////                }
////            }
////        } else {
////            DLog.d("@@@over");
////            over();
////        }
//
//        over();
//    }

    protected void over() {

        DLog.d("@@@" + getSupportFragmentManager().getBackStackEntryCount()
                + " " + doubleBackToExitPressedOnce);


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
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 500);

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


    public void launchER(String URL) {
        __url__ = URL;
        mNav.replaceWebFragment(WebFragment.newInstance(URL, aaa), R.id.container);
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
    public void dappend(String var0) {
        //DLog.d("@@@" + var0 + "\n");
    }


    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putBoolean(P.KEY_ROTATED, rotated);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rotated = savedInstanceState.getBoolean(P.KEY_ROTATED, false);
    }

}
