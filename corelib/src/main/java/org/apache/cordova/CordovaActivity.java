package org.apache.cordova;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;

import org.apache.P;
import org.apache.Utils;
import org.apache.cordova.domen.BodyClass;
import org.apache.cordova.domen.Dataset;
import org.apache.cordova.view.BetView;
import org.apache.cordova.view.GameView;
import org.apache.mvp.presenter.MainPresenter;
import org.jetbrains.annotations.NotNull;

public abstract class CordovaActivity extends AppCompatActivity
        implements org.apache.mvp.MainView//, WebFragment.Lecallback
{

    private ViewGroup container;
    private GameView gameView;
    private BetView webView2;

    MainPresenter presenter;
    private final GConfig aaa;


    private final ChromeView mmm = new ChromeView() {
        @Override
        public void onPageStarted() {
            //DLog.d("@@@@@@@@@@@@@@@@@" );
        }

        @Override
        public void onPageFinished(WebView view, String url) {
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
        public void webClientError(int errorCode, String description, String failingUrl) {
            makeScreen(new Dataset(ScreenType.GAME_VIEW, null));
        }

        @Override
        public void mAcceptPressed(String url) {

        }

        @Override
        public void eventRequest(BodyClass bodyClass) {

        }
    };

    public CordovaActivity() {
        aaa = GCfc();
    }

    protected abstract GConfig GCfc();

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
        container.addView(gameView);


        webView2 = new BetView(this);
        container.addView(webView2);

        presenter.a123(new ChromeView() {
            @Override
            public void onPageStarted() {

            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void webClientError(int errorCode, String description, String failingUrl) {
                if (BuildConfig.DEBUG) {
                    //@@
                }
            }

            @Override
            public void mAcceptPressed(String url) {

            }

            @Override
            public void eventRequest(BodyClass bodyClass) {

            }
        }, gameView);
        //presenter.makeFileSelector21_x(gameView);
        presenter.a123(mmm, webView2);

        makeScreen(new Dataset(ScreenType.GAME_VIEW, null));

        //if (BuildConfig.DEBUG) {
        //makeDemoToolbar();
        //}
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOptions);

        //Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();

        if (!rotated() && isOnline(this)) {
            presenter.init0(this);
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void hideProgressBar() {
    }

//    private void makeDemoToolbar() {
//        // Create a LinearLayout with horizontal orientation for RadioButtons
//        LinearLayout radioLayout = new LinearLayout(this);
//        radioLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        radioLayout.setOrientation(LinearLayout.HORIZONTAL);
//        radioLayout.setGravity(Gravity.CENTER);
//
//
//        Button button = new Button(this);
//        button.setText("GAME");
//        button.setGravity(Gravity.CENTER);
//
//        Button button1 = new Button(this);
//        button1.setText("WEB");
//        button1.setGravity(Gravity.CENTER);
//
//
//        // Create a RadioGroup for switching WebView visibility
//        LinearLayout layout = new LinearLayout(this);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//        layout.setGravity(Gravity.CENTER);
//
//        layout.addView(button);
//        layout.addView(button1);
//
////        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
////
////        });
//
//        button.setOnClickListener(v -> {
//            makeScreen(new Dataset(ScreenType.GAME_VIEW, null));
//        });
//        button1.setOnClickListener(v -> {
//            //presenter.wrapContentRequest();
//            makeScreen(new Dataset(ScreenType.WEB_VIEW, "https://google.com"));
//        });
//
//        // Add RadioGroup to LinearLayout
//        radioLayout.addView(layout);
//
//        // Add LinearLayout to the main layout container
//        container.addView(radioLayout);
//
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) radioLayout.getLayoutParams();
//        layoutParams.gravity = Gravity.BOTTOM;
//        radioLayout.setLayoutParams(layoutParams);
//    }

    protected boolean rotated1 = false;

    @Override
    public void makeScreen(Dataset screen) {
        if (screen.screenType == ScreenType.GAME_VIEW) {
            if (orientation404() != null && this.getRequestedOrientation() != orientation404()) {
                this.setRequestedOrientation(orientation404());
                //@@-- rotated1 = true;
            }
            boolean a = true;
            webView2.setVisibility(!a ? View.VISIBLE : View.GONE);
            gameView.setVisibility(a ? View.VISIBLE : View.GONE);
        } else if (screen.screenType == ScreenType.WEB_VIEW) {

            String url = screen.url;
            //url = "https://google.com/6665555";
            //url="";//
            //url = "https://m.aliexpress.ru/499900000000000";
            if (TextUtils.isEmpty(url)) {
                boolean a = true;
                webView2.setVisibility(!a ? View.VISIBLE : View.GONE);
                gameView.setVisibility(a ? View.VISIBLE : View.GONE);
            } else {

                if (orientationWeb() != null && this.getRequestedOrientation() != orientationWeb()) {
                    this.setRequestedOrientation(orientationWeb());
                    //@@-- rotated = true;
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url0 = MainPresenter.makeUrl(url, preferences, this);
                //DLog.d("{@@@}" + url0);
                webView2.loadUrl(url0);
                boolean a = false;
                webView2.setVisibility(!a ? View.VISIBLE : View.GONE);
                gameView.setVisibility(a ? View.VISIBLE : View.GONE);
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources (destroy WebViews)
        presenter.onDestroy();

        gameView.destroy();
        webView2.destroy();

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
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(),
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
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1400);
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
