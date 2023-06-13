package com.walhalla.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

public class PackageUtils {

    @SuppressLint("PackageManagerGetSignatures")
    public static boolean isPackageInstalled(Context context, String packageName) {
        boolean found = true;
        PackageManager manager = context.getPackageManager();
        try {
            manager.getPackageInfo(packageName,
                    //PackageManager.GET_ACTIVITIES
                    PackageManager.GET_SIGNATURES
            );
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }

    @SuppressLint("PackageManagerGetSignatures")
    public static boolean isPackageInstalledForLaunch(Context context, String packageName) {
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
}
