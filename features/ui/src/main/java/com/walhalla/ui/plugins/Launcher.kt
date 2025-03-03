package com.walhalla.ui.plugins

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.walhalla.ui.UConst

object Launcher {
    @JvmStatic
    fun openMarketApp(context: Context, packageName: String) {
        try {
            val uri = Uri.parse(com.walhalla.ui.UConst.MARKET_CONSTANT + packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage(Module_U.PKG_NAME_VENDING)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.applicationContext.startActivity(intent)
        } catch (anfe: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "http://play.google.com/store/apps/details?id=$packageName"
                        )
                    )
                )
            } catch (a: ActivityNotFoundException) {
                openBrowser(context, com.walhalla.ui.UConst.GOOGLE_PLAY_CONSTANT + packageName)
            }
        }
    }

    fun rateUs(context: Context) {
        val packageName = context.packageName
        openMarketApp(context, packageName)
    }

    fun openBrowser(context: Context, url: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show()
        }
    }
}
