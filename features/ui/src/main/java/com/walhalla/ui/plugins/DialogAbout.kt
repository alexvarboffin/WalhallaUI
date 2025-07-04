package com.walhalla.ui.plugins

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.walhalla.ui.DLog
import com.walhalla.ui.R
import com.walhalla.ui.plugins.Module_U.isFromGooglePlay
import com.walhalla.shared.R as sharedR

import java.util.Calendar

object DialogAbout {
    @JvmStatic
    fun aboutDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        //&#169; - html
        val title = "\u00a9 " + year + " " + context.getString(sharedR.string.play_google_pub)

        val mView = LayoutInflater.from(context).inflate(R.layout.about, null)
        val dialog = AlertDialog.Builder(context)
            .setTitle(null)
            .setCancelable(true)
            .setIcon(null)

            .setNegativeButton(sharedR.string.action_discover_more_app) {
                dialog1: DialogInterface?, which: Int -> Module_U.moreApp(context) }
            .setPositiveButton(android.R.string.ok, null)

            .setView(mView)
            .create()
        mView.setOnClickListener { v: View? -> dialog.dismiss() }
        val textView = mView.findViewById<TextView>(R.id.about_version)
        textView.text = DLog.getAppVersion(context)
        val _c = mView.findViewById<TextView>(R.id.about_copyright)
        _c.text = title
        val logo = mView.findViewById<ImageView>(R.id.aboutLogo)
        logo.setOnLongClickListener { v: View? ->
            _c.text = _o(context)
            false
        }
        //dialog.setButton();
        dialog.show()
    }



    private fun _o(context: Context): String {
        val appInfo: ApplicationInfo
        var minSdk = -1
        var targetSdk = -1

        try {
            appInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                minSdk = appInfo.minSdkVersion
            }
            targetSdk = appInfo.targetSdkVersion
        } catch (e: PackageManager.NameNotFoundException) {
            DLog.handleException(e)
        }

        var _o = "[+]gp->" + isFromGooglePlay(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _o = _o + ", category->" + context.applicationInfo.category
        }
        _o = ("""
     $_o, Device SDK: ${Build.VERSION.SDK_INT}
     minSdk: $minSdk
     targetSdk: $targetSdk
     compileSdk: Not available at runtime (compile-time value)
     """.trimIndent())
        return _o
    }


}