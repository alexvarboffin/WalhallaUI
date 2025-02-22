package com.kworkapp.audiogid

import androidx.multidex.MultiDexApplication
import com.walhalla.ui.DLog.d


class MyApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        d("@@@@" + this.hashCode())
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
