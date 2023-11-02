//package com.walhalla.landing;
//
//import android.graphics.Color;
//import android.os.Build;
//import android.webkit.CookieManager;
//import android.webkit.GeolocationPermissions;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//
//import androidx.appcompat.UWView;
//
//public class WebUtils {
//
//    public static void a123(ChromeView chromeView, UWView mView) {
//        WebSettings settings = mView.getSettings();
//        settings.setSupportZoom(false);
//        settings.setDefaultTextEncodingName("utf-8");
//        settings.setLoadWithOverviewMode(true);
//        settings.getLoadsImagesAutomatically();
//        settings.setGeolocationEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setDomStorageEnabled(true);
//        //webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//        settings.setBuiltInZoomControls(false);
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        mView.getSettings().setJavaScriptEnabled(true);
//        mView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mView.getSettings().setAllowFileAccess(true);
//        mView.getSettings().setAllowContentAccess(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            mView.getSettings().setAllowFileAccessFromFileURLs(true);
//            mView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        }
//
//        //"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 7 Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.78 Safari/537.36 OPR/30.0.1856.93524"
//        //System.getProperty("http.agent")
//        String tmp = mView.getSettings().getUserAgentString();
//        mView.getSettings().setUserAgentString(tmp.replace("; wv)", ")"));
//
//        if(BuildConfig.DEBUG){
//            mView.setBackgroundColor(Color.parseColor("#80000000"));
//        }
//        mView.setWebViewClient(new CustomWebViewClient(mView, chromeView));
//        mView.setWebChromeClient(new WebChromeClient() {
//            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                callback.invoke(origin, true, false);
//            }
//        });
//
//        CookieManager.getInstance().setAcceptCookie(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CookieManager.getInstance().setAcceptThirdPartyCookies(mView, true);
//        }
//
////        __mView.addJavascriptInterface(
////                new MyJavascriptInterface(CompatActivity.this, __mView), "JSInterface");
////@@        mView.addJavascriptInterface(new MyJavascriptInterface(mView.getContext(), mView), "Client");
//    }
//}
