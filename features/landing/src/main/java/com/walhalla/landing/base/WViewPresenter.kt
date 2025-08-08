package com.walhalla.landing.base

<<<<<<< HEAD
import android.app.Activity
=======
import android.annotation.SuppressLint
>>>>>>> 20de270971d5a1c7985c24144b8970fa36532fba
import android.webkit.WebBackForwardList
import android.widget.RelativeLayout
import com.walhalla.landing.activity.WPresenter
import com.walhalla.landing.activity.WebActivity
import com.walhalla.webview.ChromeView

<<<<<<< HEAD
class WViewPresenter(private val activity: Activity, private val config: ActivityConfig) :
    AbstractWVPresenter() {
    var dynamicWebView: DynamicWebView = DynamicWebView(activity, config)

    //    private SwipeRefreshLayout swipe;
    init {
        dynamicWebView.callback = (activity as UWVlayout.UWVlayoutCallback)
=======
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
>>>>>>> 20de270971d5a1c7985c24144b8970fa36532fba
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
<<<<<<< HEAD
//        if (config.isProgressEnabled()) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.progressBar.setIndeterminate(true);
//        }
        (activity as ChromeView).onPageStarted(url)
=======
        activity.onPageStarted(url)
>>>>>>> 20de270971d5a1c7985c24144b8970fa36532fba
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

