package com.walhalla.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Launcher;

public class PackageUtils {
    public static void launchOrOpenPlayStore(Context context, String packageName) {
        if (isAppInstalledActivities(context, packageName)) {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            Launcher.openMarketApp(context, packageName);
        }
    }

    public static boolean isAppInstalledActivities(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getAppVersion(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            DLog.handleException(e);
            return null;
        }
    }


    //isPackageInstalledForLaunch
    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            manager.getPackageInfo(packageName,
                    //PackageManager.GET_ACTIVITIES
                    PackageManager.GET_SIGNATURES
            );
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isPackageInstalledForLaunch(Context context, String ruokandroid) {
        return isPackageInstalled(context, ruokandroid);
    }
}
