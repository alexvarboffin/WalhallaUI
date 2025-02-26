package com.walhalla.permissionresolver.permission;

import android.annotation.SuppressLint;
import android.os.Build;


public class IOUtils {

    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
    public static boolean hasLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    public static boolean hasMarsallow23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
