package com.walhalla.landing;

import android.webkit.WebView;

public interface ChromeView {
    void onPageStarted(String url);

    void onPageFinished(/*WebView view, */String url);

    void webClientError(int errorCode, String description, String failingUrl);

    void removeErrorPage();

    void setErrorPage();

    //void mAcceptPressed(String url);

    //void eventRequest(BodyClass bodyClass);
}