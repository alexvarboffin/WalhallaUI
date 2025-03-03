package com.walhalla.landing.base

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.WebBackForwardList
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.UWView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.walhalla.landing.activity.WPresenter
import com.walhalla.webview.ChromeView

class WViewPresenterOld(
    private val chromeView: ChromeView,
    private val wPresenter: WPresenter,
    private val config: ActivityConfig
) :
    AbstractWVPresenter() {
    var dynamicWebView: DynamicWebViewOld? = null
    private var swipe: SwipeRefreshLayout? = null

    fun loadUrl(url: String) {
//        if (config.isProgressEnabled()) {
//            binding.progressBar.setVisibility(View.VISIBLE);
//            binding.progressBar.setIndeterminate(true);
//        }
        chromeView.onPageStarted(url)
        dynamicWebView!!.webView.loadUrl(url)
    }

    fun generateViews(context: Context, parent: RelativeLayout) {
        // Создайте экземпляр DynamicWebView и получите его WebView
        dynamicWebView = DynamicWebViewOld(context)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dynamicWebView!!.webView.layoutParams = lp
        if (config.isSwipeEnabled) {
            swipe = SwipeRefreshLayout(context)
            swipe!!.layoutParams = lp
            parent.addView(swipe)
            swipe!!.addView(dynamicWebView!!.webView)
            swipe!!.isRefreshing = false
            swipeWebViewRef(swipe!!, dynamicWebView!!.webView)
        } else {
            parent.addView(dynamicWebView!!.webView)
        }

        //mWebView.setBackgroundColor(Color.BLACK);
        // register class containing methods to be exposed to JavaScript
        wPresenter.a123(chromeView, dynamicWebView!!.webView)
    }


    fun refreshWV() {
        dynamicWebView!!.webView.reload()
    }

    fun canGoBack(): Boolean {
        return dynamicWebView!!.webView.canGoBack()
    }

    fun goBack() {
        dynamicWebView!!.webView.goBack()
    }


    private fun swipeWebViewRef(swipe: SwipeRefreshLayout, webView: UWView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener { view: View, i: Int, i1: Int, i2: Int, i3: Int ->
                if (!view.canScrollVertically(-1)) {
                    swipe.isEnabled = true
                } else {
                    swipe.isEnabled = false
                }
            }
        }
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            webView.reload()
        }
    }

    fun hideSwipeRefreshing() {
        if (config.isSwipeEnabled) {
            swipe!!.isRefreshing = false //modify if needed
        }
    }


    fun copyBackForwardList(): WebBackForwardList {
        return dynamicWebView!!.webView.copyBackForwardList()
    }
}
