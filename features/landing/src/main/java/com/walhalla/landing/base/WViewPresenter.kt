package com.walhalla.landing.base

import android.annotation.SuppressLint
import android.webkit.WebBackForwardList
import android.widget.RelativeLayout
import com.walhalla.landing.activity.WPresenter
import com.walhalla.landing.activity.WebActivity
import com.walhalla.webview.ChromeView

object DynamicWebViewHolder {
    @SuppressLint("StaticFieldLeak")
    private var instance: DynamicWebView? = null

    fun getInstance(activity: WebActivity, config: ActivityConfig): DynamicWebView {
        if (instance == null) {
            instance = DynamicWebView(activity, config).apply {
                callback = activity
            }
        }
        return instance!!
    }

    fun reset() {
        instance = null
    }
}

class WViewPresenter(private val activity: WebActivity, private val config: ActivityConfig) :
    AbstractWVPresenter() {

    val dynamicWebView: DynamicWebView =
        DynamicWebViewHolder.getInstance(activity, config)

    fun loadUrl(url: String) {
        activity.onPageStarted(url)
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
            // swipe.setRefreshing(false); // может быть реализовано позже
        }
    }

    fun copyBackForwardList(): WebBackForwardList {
        return dynamicWebView.webView.copyBackForwardList()
    }

    fun generateViews(activity: ChromeView?, contentMain: RelativeLayout, p: WPresenter) {
        dynamicWebView.generateViews(activity, contentMain, p)
    }
}

