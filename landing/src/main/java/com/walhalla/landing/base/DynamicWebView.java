package com.walhalla.landing.base;

import android.content.Context;

import androidx.appcompat.UWView;

public class DynamicWebView {

    private final UWView webView;

    public DynamicWebView(Context context) {
        webView = new UWView(context);
        // Настройка WebView
        //webView.getSettings().setJavaScriptEnabled(true);
    }

    public UWView getWebView() {
        return webView;
    }

    // Дополнительные методы для настройки и использования WebView
}
