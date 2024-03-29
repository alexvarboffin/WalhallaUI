package org.apache.cordova;

import android.webkit.WebView;

import org.apache.cordova.domen.BodyClass;

public interface ChromeView {
    void onPageStarted(String url);

    void onPageFinished(String url);

    void webClientError(int errorCode, String description, String failingUrl);

    void mAcceptPressed(String url);

    void eventRequest(BodyClass bodyClass);


    void setErrorPage();

    void removeErrorPage();
}