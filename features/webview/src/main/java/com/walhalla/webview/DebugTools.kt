package com.walhalla.webview

import android.net.Uri
import android.util.Log

object DebugTools {
    private val TAG: String = DebugTools::class.java.simpleName

    fun printParams(s: String, url: String) {
        if (BuildConfig.DEBUG) {
            val uri = Uri.parse(url)
            val domain = uri.host
            Log.d(TAG, "$s $url $domain")
        }
    }
}
