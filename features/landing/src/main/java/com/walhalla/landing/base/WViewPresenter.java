package com.walhalla.landing.base;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.RelativeLayout;

import androidx.appcompat.UWView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.walhalla.landing.activity.WPresenter;
import com.walhalla.landing.activity.WebActivity;
import com.walhalla.landing.activity.WebActivityInterface;
import com.walhalla.webview.ChromeView;

public class WViewPresenter extends AbstractWVPresenter{

    private final ActivityConfig config;
    private final WebActivity activity;

    public DynamicWebView getDynamicWebView() {
        return dynamicWebView;
    }

    DynamicWebView dynamicWebView;
//    private SwipeRefreshLayout swipe;

    public WViewPresenter(WebActivity activity, ActivityConfig config) {
        this.activity = activity;
        this.config = config;
        this.dynamicWebView = new DynamicWebView(activity,config);
        this.dynamicWebView.setCallback(activity);
    }

    public void loadUrl(String url) {
//        if (config.isProgressEnabled()) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.progressBar.setIndeterminate(true);
//        }
        activity.onPageStarted(url);
        dynamicWebView.getWebView().loadUrl(url);
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



    public void hideSwipeRefreshing() {
        if (config.isSwipeEnabled()) {
            //swipe.setRefreshing(false);//modify if needed
        }
    }

    public WebBackForwardList copyBackForwardList() {
        return dynamicWebView.getWebView().copyBackForwardList();
    }

    public void generateViews(ChromeView activity, RelativeLayout contentMain, WPresenter p) {
        dynamicWebView.generateViews(activity, contentMain, p);
    }
}
