package org.apache.cordova;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;
import com.walhalla.webview.ChromeView;
import com.walhalla.webview.ReceivedError;

import org.apache.cordova.domen.BodyClass;

import org.apache.cordova.domen.ScreenType;
import org.apache.cordova.domen.UIVisibleDataset;
import org.apache.cordova.repository.AbstractDatasetRepository;
import org.apache.cordova.utility.DemoUtils;
import org.apache.cordova.utility.NetUtils;
import org.apache.cordova.view.GameView;
import org.apache.mvp.presenter.MainPresenter;

public abstract class CordovaActivity extends AppCompatActivity implements org.apache.mvp.MainView//, WebFragment.Lecallback
{

    private ViewGroup container;
    private GameView gameView;
    //private BetView webView2;

    MainPresenter presenter;
    private final Gfg aaa;


    private final ChromeView chromeView = new ChromeView() {
        @Override
        public void onPageStarted(String s) {
            //DLog.d("@@@@@@@@@@@@@@@@@" );
        }

        @Override
        public void onPageFinished(String url) {
            presenter.saveFirstPageIfValid(url);

//                String title = view.getTitle();
//                if (webTitle() && !TextUtils.isEmpty(title) && toolbar != null) {
//                    if (title != null && title.startsWith(view.getUrl())) {
//                        toolbar.setSubtitle(title);
//                    }
//                }
            hideProgressBar();
        }

        @Override
        public void webClientError(ReceivedError failure) {
            makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));
        }




        public void eventRequest(BodyClass bodyClass) {

        }



        @Override
        public void removeErrorPage() {
            DLog.d("=====");
            //binding.contentFake.setVisibility(View.GONE);
        }

        @Override
        public void setErrorPage(ReceivedError receivedError) {
//binding.contentFake.setVisibility(View.VISIBLE);
        }

        @Override
        public void openBrowser(String url) {

        }
    };

    private DynamicWebViewHolder dynamicWebView;

    public CordovaActivity() {
        aaa = GCfc();
    }

    protected abstract Gfg GCfc();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler(Looper.getMainLooper());
        presenter = new MainPresenter(this, this, aaa, handler, makeTracker());

        // Create the main layout container
        container = new FrameLayout(this);
        container.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(container);

        // Create WebView instances
        gameView = new GameView(this);
        rootContainer(gameView);


//        webView2 = new BetView(this);
//        rootContainer(webView2);


        generateViews(this, container);


//        presenter.a123(new ChromeView() {
//            @Override
//            public void onPageStarted(String url) {
//
//            }
//
//            @Override
//            public void onPageFinished(String url) {
//
//            }
//
//            @Override
//            public void webClientError(int errorCode, String description, String failingUrl) {
//                if (BuildConfig.DEBUG) {
//                    //@@
//                }
//            }
//
//            @Override
//            public void mAcceptPressed(String url) {
//
//            }
//
//            @Override
//            public void eventRequest(BodyClass bodyClass) {
//
//            }
//        }, gameView);
        //presenter.makeFileSelector21_x(gameView);
        gameView.getSettings().setJavaScriptEnabled(true);


        //@@@@@presenter.a123(chromeView, webView2);

        makeScreen(new UIVisibleDataset(ScreenType.GAME_VIEW, null));

        //if (BuildConfig.DEBUG) {
            DemoUtils.makeDemoToolbar(this);
        //}
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOptions);

        //Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();

        if (!rotated() && NetUtils.isOnline(this)) {
            presenter.init0(this);
        }
    }

    protected abstract AbstractDatasetRepository makeTracker();

    private void generateViews(CordovaActivity cordovaActivity, ViewGroup parent) {
        // Создайте экземпляр DynamicWebViewHolder и получите его WebView
        dynamicWebView = new DynamicWebViewHolder(this);
        dynamicWebView.generateViews(cordovaActivity, parent, presenter, chromeView, aaa.isSwipeEnabled());
    }




    private void hideProgressBar() {
    }



    public void rootContainer(View view) {
        container.addView(view);
    }

    protected boolean rotated1 = false;

    @Override
    public void makeScreen(UIVisibleDataset screen) {
        if (screen.getScreenType() == ScreenType.GAME_VIEW) {
            if (orientation404() != null && this.getRequestedOrientation() != orientation404()) {
                this.setRequestedOrientation(orientation404());
                //@@-- rotated1 = true;
            }
            boolean a = true;
            webView2setVisibility(!a ? View.VISIBLE : View.GONE);
            gameView.setVisibility(a ? View.VISIBLE : View.GONE);
        } else if (screen.getScreenType() == ScreenType.WEB_VIEW) {
            String url = screen.getUrl();
            //url = "https://google.com/6665555";
            //url="";//
            //url = "https://m.aliexpress.ru/499900000000000";
            if (TextUtils.isEmpty(url)) {
                boolean a = true;
                webView2setVisibility(!a ? View.VISIBLE : View.GONE);
                gameView.setVisibility(a ? View.VISIBLE : View.GONE);
            } else {

                if (orientationWeb() != null && this.getRequestedOrientation() != orientationWeb()) {
                    this.setRequestedOrientation(orientationWeb());
                    //@@-- rotated = true;
                }
                //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                TPreferences preferences = TPreferences.getInstance(this);
                String url0 = MainPresenter.makeUrl(url,  preferences,this);
                //DLog.d("{@@@}" + url0);
                webView2loadUrl(url0);
                boolean a = false;
                webView2setVisibility(!a ? View.VISIBLE : View.GONE);
                gameView.setVisibility(a ? View.VISIBLE : View.GONE);
            }

        }
    }

    private void webView2loadUrl(String url0) {
        if (dynamicWebView == null) {
            return;
        }//webView2.loadUrl(url0);
        dynamicWebView.loadUrl(url0);
    }

    private void webView2setVisibility(int i) {
        if (dynamicWebView == null) {
            return;
        }
        //webView2.setVisibility
        dynamicWebView.webView2setVisibility(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources (destroy WebViews)
        presenter.onDestroy();

        gameView.destroy();

        //webView2.destroy();
        dynamicWebView.destroy();
    }

    public void loadUrl(String url) {
        if (orientation404() != null && this.getRequestedOrientation() != orientation404()) {
            this.setRequestedOrientation(orientation404());
        }
        gameView.loadUrl(url);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            this.setRequestedOrientation(newConfig.orientation);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            this.setRequestedOrientation(newConfig.orientation);
//        }
    }

//    @Override
//    protected void onSaveInstanceState(@NotNull Bundle outState) {
//        outState.putBoolean(P.KEY_ROTATED, rotated);
//        super.onSaveInstanceState(outState);
//    }

//    @Override
//    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        rotated = savedInstanceState.getBoolean(P.KEY_ROTATED, false);
//    }


    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        //Pressed back => return to home screen
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(count > 0);
        }
        if (count > 0) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            if (isFirstPage()) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;

                    // Move the task containing the MainActivity to the back of the activity stack, instead of
                    // destroying it. Therefore, MainActivity will be shown when the user switches back to the app.
                    //moveTaskToBack(true);
                    //return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
//                Toasty.custom(this, R.string.press_again_to_exit,
//                        comv19.getDrawable(this, R.drawable.ic_info),
//                        R.color.colorPrimaryDark,
//                        R.color.white, Toasty.LENGTH_SHORT, true, true).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1600);
            } else {
                // Если текущий фрагмент не является первым, вернитесь на первый фрагмент
                //viewPager2.setCurrentItem(0, true); // Установите первый фрагмент в ViewPager2
            }
        }
    }

    private boolean isFirstPage() {
        return true;
    }

    protected void onResume() {
        super.onResume();
        presenter.onResume(getIntent());
    }
}
