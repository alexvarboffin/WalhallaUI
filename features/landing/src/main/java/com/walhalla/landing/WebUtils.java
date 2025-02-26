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
//        WebSettings settings = a;
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
//        a.setJavaScriptEnabled(true);
//        a.setPluginState(WebSettings.PluginState.ON);
//        a.setAllowFileAccess(true);
//        a.setAllowContentAccess(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            a.setAllowFileAccessFromFileURLs(true);
//            a.setAllowUniversalAccessFromFileURLs(true);
//        }
//
//        //
//        //System.getProperty("http.agent")
//        String tmp = a.getUserAgentString();
//        a.setUserAgentString(tmp.replace("; wv)", ")"));
//
//        if(BuildConfig.DEBUG){
//            mView.setBackgroundColor(Color.parseColor("#80000000"));
//        }
//        mView.setWebViewClient(new CustomWebViewClient(mView, chromeView));
//        mView.setWebChromeClient(@@new WebChromeClient() {
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
