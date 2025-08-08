package com.walhalla.landing.base

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.UWView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.walhalla.landing.activity.WPresenter
import com.walhalla.landing.base.UWVlayout.UWVlayoutCallback
import com.walhalla.webview.ChromeView

class DynamicWebView(private val context: Activity, private val config: ActivityConfig) {
    //    private void showPopupMenu(Context context, View v) {
    //        PopupMenu popupMenu = new PopupMenu(context, v);
    //        popupMenu.inflate(R.menu.wv_menu);
    //        popupMenu.setOnMenuItemClickListener(item -> {
    //            int itemId = item.getItemId();
    //            if (itemId == R.id.action_home) {
    //                uwVlayout.loadUrl("http://www.example.com");
    //                return true;
    //            } else if (itemId == R.id.action_exit) {
    //                //context.finish();
    //                return true;
    //            } else {
    //                return false;
    //            }
    //        });
    //        popupMenu.show();
    //    }

    private val uwVlayout: UWVlayout = UWVlayout(context)

    private var swipe: SwipeRefreshLayout? = null


    var callback: UWVlayoutCallback? = null
        set(value) {
            field = value
            uwVlayout.setUWVCallback(value)
        }


    init {
        //        uwVlayout.setOnLongClickListener(v -> {
//            showPopupMenu(context, v);
//            return true;
//        });
        // Настройка WebView
        //webView.getSettings().setJavaScriptEnabled(true);
    }

    //    private void showPopupMenu(Context context, View v) {
    //        PopupMenu popupMenu = new PopupMenu(context, v);
    //        popupMenu.inflate(R.menu.wv_menu);
    //        popupMenu.setOnMenuItemClickListener(item -> {
    //            int itemId = item.getItemId();
    //            if (itemId == R.id.action_home) {
    //                uwVlayout.loadUrl("http://www.example.com");
    //                return true;
    //            } else if (itemId == R.id.action_exit) {
    //                //context.finish();
    //                return true;
    //            } else {
    //                return false;
    //            }
    //        });
    //        popupMenu.show();
    //    }


    val webView: UWView
        get() = uwVlayout.customWebviewLayoutBinding.inner00


    fun getParent(): UWVlayout {
        return uwVlayout
    }

    fun generateViews(chromeView: ChromeView?, parent: RelativeLayout, presenter: WPresenter) {
        // Создайте экземпляр DynamicWebView и получите его WebView
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        uwVlayout.layoutParams = lp
        if (config.isSwipeEnabled) {
            swipe = SwipeRefreshLayout(context)
            swipe!!.layoutParams = lp
            (swipe!!.parent as? ViewGroup)?.removeView(swipe)
            parent.addView(swipe)
            swipe!!.addView(uwVlayout)
            swipe!!.isRefreshing = false
            swipeWebViewRef(swipe!!, uwVlayout.customWebviewLayoutBinding.inner00)
        } else {
            (uwVlayout.parent as? ViewGroup)?.removeView(uwVlayout)
            parent.addView(uwVlayout)
        }

        //mWebView.setBackgroundColor(Color.BLACK);
        // register class containing methods to be exposed to JavaScript
        presenter.a123(chromeView, uwVlayout.customWebviewLayoutBinding.inner00)
    }

    private fun swipeWebViewRef(swipe: SwipeRefreshLayout, webView: UWView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener { view: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (!view.canScrollVertically(-1)) {
                    swipe.isEnabled = true // Разрешить обновление при прокрутке вверх
                } else {
                    if (swipe.isRefreshing) {
                        swipe.isRefreshing = false
                    } // Запретить обновление при прокрутке вниз

                    swipe.isEnabled = false
                }
            }
        }


        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            webView.reload()
        }
    }
}
