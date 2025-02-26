package org.apache.cordova;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.UWView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.walhalla.webview.ChromeView;

import org.apache.mvp.presenter.MainPresenter;

public class DynamicWebViewHolder {

    private final UWView webView;
    private View _p;

    public DynamicWebViewHolder(Context context) {
        webView = new UWView(context);

        // Настройка WebView
        //webView.getSettings().setJavaScriptEnabled(true);
    }

    public UWView getWebView() {
        return webView;
    }

    public void webView2setVisibility(int i) {
//        if (webView != null) {
//            webView.setVisibility(i);
//        }

        if (_p != null) {
            _p.setVisibility(i);
        }
    }

    public void generateViews(Context context, ViewGroup parent, MainPresenter presenter, ChromeView chromeView, boolean swipeEnabled) {


//        FrameLayout container9 = new FrameLayout(context);
//        container9.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        rootContainer(container9);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWebView().setLayoutParams(lp);
        if (swipeEnabled) {
            SwipeRefreshLayout swipe = new SwipeRefreshLayout(context);
            _p = swipe;
            swipe.setLayoutParams(lp);
            parent.addView(swipe);
            swipe.addView(getWebView());
            swipe.setRefreshing(false);
            swipe.setOnRefreshListener(() -> {
                swipe.setRefreshing(false);
                getWebView().reload();
            });
        } else {
            _p = getWebView();
            parent.addView(getWebView());
        }
        //mWebView.setBackgroundColor(Color.BLACK);
        // register class containing methods to be exposed to JavaScript
        presenter.a123(chromeView, getWebView());
    }

    public void loadUrl(String url0) {
        getWebView().loadUrl(url0);
    }

    public void destroy() {
        getWebView().destroy();
    }

    // Дополнительные методы для настройки и использования WebView
}
