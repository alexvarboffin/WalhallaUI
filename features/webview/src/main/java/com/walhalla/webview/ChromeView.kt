package com.walhalla.webview;

import android.app.Activity;

public interface ChromeView {
    void onPageStarted(String url);

    void onPageFinished(/*WebView view, */String url);

    void webClientError(ReceivedError failure);

    void removeErrorPage();

    void setErrorPage(ReceivedError receivedError);

    void openBrowser(Activity context, String url);

    //void openOauth2(Activity context, String url);

    //void mAcceptPressed(String url);

    //void eventRequest(BodyClass bodyClass);
}