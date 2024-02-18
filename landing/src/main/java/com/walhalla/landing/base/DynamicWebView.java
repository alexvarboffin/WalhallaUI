package com.walhalla.landing.base;

import android.content.Context;
import android.webkit.DownloadListener;

import androidx.appcompat.UWView;

import com.walhalla.ui.DLog;

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
