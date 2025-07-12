package com.walhalla.permissionresolver.permission

import android.annotation.SuppressLint
import android.os.Build

object IOUtils {
    @SuppressLint("ObsoleteSdkInt")
    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }
    @JvmStatic
    fun hasLolipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    @JvmStatic
    fun hasMarsallow23(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}
