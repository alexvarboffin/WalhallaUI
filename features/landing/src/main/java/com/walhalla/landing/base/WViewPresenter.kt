package com.walhalla.landing.base

import android.webkit.WebBackForwardList
import android.widget.RelativeLayout
import com.walhalla.landing.activity.WPresenter
import com.walhalla.landing.activity.WebActivity
import com.walhalla.webview.ChromeView

class WViewPresenter(private val activity: WebActivity, private val config: ActivityConfig) :
    AbstractWVPresenter() {
    var dynamicWebView: DynamicWebView = DynamicWebView(activity, config)

    //    private SwipeRefreshLayout swipe;
    init {
        dynamicWebView.setCallback(activity)
    }

    fun loadUrl(url: String) {
//        if (config.isProgressEnabled()) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.progressBar.setIndeterminate(true);
//        }
        activity.onPageStarted(url)
        dynamicWebView.getWebView().loadUrl(url)
    }

    fun refreshWV() {
        dynamicWebView.getWebView().reload()
    }

    fun canGoBack(): Boolean {
        return dynamicWebView.getWebView().canGoBack()
    }

    fun goBack() {
        dynamicWebView.webiewV.goBack()
    }


    fun hideSwipeRefreshing() {
        if (config.isSwipeEnabled) {
            //swipe.setRefreshing(false);//modify if needed
        }
    }

    fun copyBackForwardList(): WebBackForwardList {
        return dynamicWebView.webView.copyBackForwardList()
    }

    fun generateViews(activity: ChromeView?, contentMain: RelativeLayout?, p: WPresenter?) {
        dynamicWebView.generateViews(activity, contentMain, p)
    }
}
