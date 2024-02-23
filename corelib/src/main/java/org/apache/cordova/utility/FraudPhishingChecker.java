package org.apache.cordova.utility;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.walhalla.ui.DLog;

import org.apache.cordova.Chipper;

import java.util.ArrayList;
import java.util.List;

public class FraudPhishingChecker {

//    static String[] system_app = new String[]{
//            "com.google", //com.google.android
//            "com.android",
//            "android", "com.xiaomi.", "com.miui.",
//            "com.huawei.", "com.sonymobile.",
//            "com.qualcomm.", "com.mediatek",
//            "com.samsung", "com.zte.",
//            "com.sec.", "com.realme", "com.oppo"
//    };
//
//    private static boolean noneSysApp(String packageName) {
//        for (String s : system_app) {
//            if (packageName.startsWith(s)) {
//                return false;
//            }
//        }
//        return true;
//    }

    // Проверка наличия менее трех приложений, не являющихся системными
    public static boolean checkForFraudOrPhishing(Context context) {
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> nonSys = getNonSystemApps(context, packages);
        return nonSys.size() < 3;
    }

//    public static List<String> getNonSystemApps(Context context, List<ApplicationInfo> packages) {
//        final String packageName = context.getPackageName();
//        //StringBuilder installedPackageName = new StringBuilder();
//        List<String> installedPackageName = new ArrayList<>();
//
//        for (ApplicationInfo packageInfo : packages) {
//            if (!packageName.equals(packageInfo.packageName) && noneSysApp(packageInfo.packageName)) {
//                //installedPackageName.append(packageInfo.packageName).append((char) 10);
//                installedPackageName.add(packageInfo.packageName);
//            }
//            //DLog.d("1234567 Source dir : " + packageInfo.sourceDir);
//            //DLog.d("1234567 Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
//        }
//        return installedPackageName;
//    }

    public static List<String> getNonSystemApps(Context context, List<ApplicationInfo> installedApps) {
        List<String> nonSystemApps = new ArrayList<>();
        for (ApplicationInfo appInfo : installedApps) {
            // Проверка, является ли приложение системным или системным обновлением
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                nonSystemApps.add(appInfo.packageName);
            }
        }
        return nonSystemApps;
    }
}
