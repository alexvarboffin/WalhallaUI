package com.walhalla.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.walhalla.ui.DLog.handleException
import com.walhalla.ui.plugins.Launcher.openMarketApp

object PackageUtils {
    fun launchOrOpenPlayStore(context: Context, packageName: String) {
        if (isAppInstalledActivities(context, packageName)) {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } else {
            openMarketApp(context, packageName)
        }
    }

    fun isAppInstalledActivities(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun getAppVersion(context: Context, packageName: String): String? {
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            handleException(e)
            return null
        }
    }


    //isPackageInstalledForLaunch
    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        val manager = context.packageManager
        try {
            manager.getPackageInfo(
                packageName,  //PackageManager.GET_ACTIVITIES
                PackageManager.GET_SIGNATURES
            )
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun isPackageInstalledForLaunch(context: Context, ruokandroid: String): Boolean {
        return isPackageInstalled(context, ruokandroid)
    }
}
