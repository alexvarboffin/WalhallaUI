package com.walhalla.ui.plugins

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.walhalla.ui.UConst
import androidx.core.net.toUri

object Launcher {
    @JvmStatic
    fun openMarketApp(context: Context, packageName: String) {
        try {
            val uri = (UConst.MARKET_CONSTANT + packageName).toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage(Module_U.PKG_NAME_VENDING)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.applicationContext.startActivity(intent)
        } catch (anfe: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "http://play.google.com/store/apps/details?id=$packageName".toUri()
                    )
                )
            } catch (a: ActivityNotFoundException) {
                openBrowser(context, UConst.GOOGLE_PLAY_CONSTANT + packageName)
            }
        }
    }
    @JvmStatic
    fun rateUs(context: Context) {
        val packageName = context.packageName
        openMarketApp(context, packageName)
    }
    @JvmStatic
    fun openBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
        }
    }
}
