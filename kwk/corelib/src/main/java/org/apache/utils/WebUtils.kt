package org.apache.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.browser.customtabs.CustomTabsService

object WebUtils {
    //            for (ResolveInfo info : list) {
    ////            24 ResolveInfo{5f59c04 com.android.chrome/com.google.android.apps.chrome.Main m=0x208000}
    ////               ResolveInfo{a8a75dc com.android.chrome/com.google.android.apps.chrome.IntentDispatcher m=0x208000}
    //                DLog.d("@" + info.toString());
    //            }
    fun getCustomTabsPackages(context: Context): List<ResolveInfo> {
        val pm = context.packageManager
        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        // Get all apps that can handle VIEW intents.
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs: MutableList<ResolveInfo> = ArrayList()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION)
            serviceIntent.setPackage(info.activityInfo.packageName)
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info)
            }
        }
        return packagesSupportingCustomTabs
    }


    //ChatGpt notwork
    //    public boolean isChromeCustomTabsSupported() {
    //        PackageManager pm = context.getPackageManager();
    //        Intent serviceIntent = new Intent().setClassName("com.android.chrome", "com.google.android.apps.chrome.CustomTabsService");
    //        return pm.resolveService(serviceIntent, 0) != null;
    //    }
    fun isChromeCustomTabsSupported(context: Context): Boolean {
        return getCustomTabsPackages(context).size > 0
    }
}
