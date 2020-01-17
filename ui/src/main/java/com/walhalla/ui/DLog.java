package com.walhalla.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DLog {

    // █  ▄| 
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static String TAG = "@@@";


    /**
     * Log Level Error
     **/
    public static void e(String message) {
        if (DEBUG) {
            Log.d(TAG, buildLogMsg(message));
        }
    }

    /**
     * Log Level Warning
     **/
    public static void w(String message) {
        if (DEBUG) Log.w(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Information
     **/
    public static void i(String message) {
        if (DEBUG) Log.i(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Debug
     **/
    public static void d(String message) {
        if (DEBUG) Log.d(TAG, buildLogMsg(message));
    }

    /**
     * Log Level Verbose
     **/
    public static void v(String message) {
        if (DEBUG) Log.v(TAG, buildLogMsg(message));
    }


    private static String buildLogMsg(String message) {

        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();


        //Class<? extends StackTraceElement> obj = ste.getClass();
        //if (ste.getClass() instanceof Fragment) {
        //    sb.append("#");
        //}
        sb.append("█ ");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append(" ● ");
        sb.append(message);
        sb.append(" █");

        return sb.toString();

    }


    public static boolean nonNull(Object o) {
        if (DEBUG) {
            Log.i(TAG, "nonNull: " + ((o == null) ? o : o.toString()));
        }
        return o != null;
    }

//    context.getString(R.string.app_name) + " v"
// + versionName + " (build " + versionCode + ")"

    public static String getAppVersion(Context context) {
        try {

//            versionCode = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(),
//                            0).versionCode;

            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static String timeStamp(@NonNull Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf = new ZipFile(applicationInfo.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();
            String s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
            zf.close();
            return s;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static void handleException(Exception e) {
        DLog.i(e.getClass().getSimpleName() + "-->" + (e.getLocalizedMessage() == null ? "NuLL" : e.getLocalizedMessage()));
    }
}
