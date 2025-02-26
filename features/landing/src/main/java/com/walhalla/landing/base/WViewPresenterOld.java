package com.walhalla.landing.base;

import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.UWView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.walhalla.webview.ChromeView;
import com.walhalla.landing.activity.WPresenter;

public class WViewPresenterOld extends AbstractWVPresenter {

    private final ActivityConfig config;
    private final WPresenter wPresenter;
    private final ChromeView chromeView;

    DynamicWebViewOld dynamicWebView;
    private SwipeRefreshLayout swipe;

    public WViewPresenterOld(ChromeView chromeView, WPresenter presenter, ActivityConfig config) {
        this.chromeView = chromeView;
        this.wPresenter = presenter;
        this.config = config;
    }

    public void loadUrl(String url) {
//        if (config.isProgressEnabled()) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.progressBar.setIndeterminate(true);
//        }
        chromeView.onPageStarted(url);
        dynamicWebView.getWebView().loadUrl(url);
    }

    public void generateViews(Context context, RelativeLayout parent) {
        // Создайте экземпляр DynamicWebView и получите его WebView
        dynamicWebView = new DynamicWebViewOld(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dynamicWebView.getWebView().setLayoutParams(lp);
        if (config.isSwipeEnabled()) {
            swipe = new SwipeRefreshLayout(context);
            swipe.setLayoutParams(lp);
            parent.addView(swipe);
            swipe.addView(dynamicWebView.getWebView());
            swipe.setRefreshing(false);
            swipeWebViewRef(swipe, dynamicWebView.getWebView());
        } else {
            parent.addView(dynamicWebView.getWebView());
        }

        //mWebView.setBackgroundColor(Color.BLACK);
        // register class containing methods to be exposed to JavaScript
        wPresenter.a123(chromeView, dynamicWebView.getWebView());
    }


    public void refreshWV() {
        dynamicWebView.getWebView().reload();
    }

    public boolean canGoBack() {
        return dynamicWebView.getWebView().canGoBack();
    }

    public void goBack() {
        dynamicWebView.getWebView().goBack();
    }


    private void swipeWebViewRef(SwipeRefreshLayout swipe, UWView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
                if (!view.canScrollVertically(-1)) {
                    swipe.setEnabled(true);
                } else {
                    swipe.setEnabled(false);
                }
            });
        }
        swipe.setOnRefreshListener(() -> {
            swipe.setRefreshing(false);
            webView.reload();
        });
    }
    
    public void hideSwipeRefreshing() {
        if (config.isSwipeEnabled()) {
            swipe.setRefreshing(false);//modify if needed
        }
    }


    public WebBackForwardList copyBackForwardList() {
        return dynamicWebView.getWebView().copyBackForwardList();
    }

}
