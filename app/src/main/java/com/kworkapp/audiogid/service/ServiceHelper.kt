package com.kworkapp.audiogid.service

import android.app.ActivityManager
import android.content.Context

object ServiceHelper {
    fun isMyServiceRunning(activity: Context, serviceClass: Class<*>): Boolean {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (manager != null) {
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        }
        return false
    }
}
