package com.walhalla.landing.base

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.UWView
import androidx.appcompat.widget.PopupMenu
import com.walhalla.landing.BuildConfig
import com.walhalla.landing.R
import com.walhalla.landing.databinding.CustomWebviewLayoutBinding

class UWVlayout : RelativeLayout //ChromeView
{
    fun setUWVCallback(callback: UWVlayoutCallback?) {
        this.callback = callback
    }
    val webView: UWView
        get() = customWebviewLayoutBinding!!.inner00
    var callback: UWVlayoutCallback? = null

    interface UWVlayoutCallback {
        fun closeApplication()

        fun copyToClipboard(url: String)
    }

    val buttonMenu: ImageView
        get() = customWebviewLayoutBinding!!.buttonMenu

    private var customWebviewLayoutBinding: CustomWebviewLayoutBinding? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = LayoutInflater.from(context)
        customWebviewLayoutBinding = CustomWebviewLayoutBinding.inflate(
            inflater,
            this, true
        )
        customWebviewLayoutBinding!!.buttonMenu.setOnClickListener { v: View ->
            showPopupMenu(
                context,
                v
            )
        }

        if (BuildConfig.DEBUG) {
            setBackgroundColor(Color.parseColor("#80770000"))
        }
    }

    private fun showPopupMenu(context: Context, v: View) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.inflate(R.menu.wv_menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.action_home) {
                val homeUrl = webView.originalUrl
                webView.loadUrl(homeUrl!!)
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.action_exit) {
                if (callback != null) {
                    callback!!.closeApplication()
                }
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.action_url_copy) {
                if (callback != null) {
                    val url = webView.url
                    if (url != null) {
                        callback!!.copyToClipboard(url)
                    }
                }
                return@setOnMenuItemClickListener true
            } else {
                return@setOnMenuItemClickListener false
            }
        }
        popupMenu.show()
    }


}
