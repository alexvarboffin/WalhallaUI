package com.walhalla.landing.base

import android.app.Activity
import android.webkit.WebBackForwardList
import android.widget.RelativeLayout
import com.walhalla.landing.activity.WPresenter
import com.walhalla.landing.activity.WebActivity
import com.walhalla.webview.ChromeView

class WViewPresenter(private val activity: Activity, private val config: ActivityConfig) :
    AbstractWVPresenter() {
    var dynamicWebView: DynamicWebView = DynamicWebView(activity, config)

    //    private SwipeRefreshLayout swipe;
    init {
        dynamicWebView.callback = (activity as UWVlayout.UWVlayoutCallback)
    }

    fun loadUrl(url: String) {
//        if (config.isProgressEnabled()) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.progressBar.setIndeterminate(true);
//        }
        (activity as ChromeView).onPageStarted(url)
        dynamicWebView.webView.loadUrl(url)
    }

    fun refreshWV() {
        dynamicWebView.webView.reload()
    }

    fun canGoBack(): Boolean {
        return dynamicWebView.webView.canGoBack()
    }

    fun goBack() {
        dynamicWebView.webView.goBack()
    }


    fun hideSwipeRefreshing() {
        if (config.isSwipeEnabled) {
            //swipe.setRefreshing(false);//modify if needed
        }
    }

    fun copyBackForwardList(): WebBackForwardList {
        return dynamicWebView.webView.copyBackForwardList()
    }

    fun generateViews(activity: ChromeView?, contentMain: RelativeLayout, p: WPresenter) {
        dynamicWebView.generateViews(activity, contentMain, p)
    }
}
