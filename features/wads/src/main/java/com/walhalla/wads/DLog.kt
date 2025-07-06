package com.walhalla.wads

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.zip.ZipFile

/**
 * Created by huangwei on 2015/5/25.
 */
object DLog {
    const val DEBUG: Boolean = true

    private const val TAG = "@@@"


    /**
     * Log Level Error
     */
    fun e(message: String) {
        if (DEBUG) {
            Log.d(TAG, buildLogMsg(message))
        }
    }

    /**
     * Log Level Warning
     */
    fun w(message: String) {
        if (DEBUG) Log.w(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Information
     */
    fun i(message: String) {
        if (DEBUG) Log.i(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Debug
     */
    @JvmStatic
    fun d(message: String) {
        if (DEBUG) Log.d(TAG, buildLogMsg(message))
    }

    /**
     * Log Level Verbose
     */
    fun v(message: String) {
        if (DEBUG) d(buildLogMsg(message))
    }


    private fun buildLogMsg(message: String): String {
        val ste = Thread.currentThread().stackTrace[4]

        val sb = StringBuilder()


        //Class<? extends StackTraceElement> obj = ste.getClass();
        //if (ste.getClass() instanceof Fragment) {
        //    sb.append("#");
        //}
        sb.append("█ ")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append("::")
        sb.append(ste.methodName)
        sb.append(" ● ")
        sb.append(message)
        sb.append(" █")

        return sb.toString()
    }


    fun nonNull(o: Any?): Boolean {
        if (DEBUG) {
            Log.i(TAG, "nonNull: " + (if ((o == null)) o else o.toString()))
        }
        return o != null
    }

    //    context.getString(R.string.app_name) + " v"
    // + versionName + " (build " + versionCode + ")"
    fun getAppVersion(context: Context): String? {
        return try {
            //            versionCode = context.getPackageManager()
            //                    .getPackageInfo(context.getPackageName(),
            //                            0).versionCode;

            context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    fun timeStamp(context: Context): String {
        try {
            val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
            val zf = ZipFile(applicationInfo.sourceDir)
            val ze = zf.getEntry("classes.dex")
            val time = ze.time
            val s = SimpleDateFormat.getInstance().format(Date(time))
            zf.close()
            return s
        } catch (e: Exception) {
            return "Unknown"
        }
    }

    @JvmStatic
    fun handleException(e: Exception) {
        Log.d(
            TAG, buildLogMsg(
                (e.javaClass.simpleName
                        + " @@@ EX @@@ " + (if (e.message == null) "NULL" else  //ff(
                    e.message //.replace("Column{", "\n")
                        //)
                        ))
            )
        )
    }

    //    private static String ff(String replace) {
    //
    //        if (replace.contains("Found:")) {
    //            String[] mm = replace.replace("Expected:", "").split("Found:");
    //            String[] c = mm[0].split(",");
    //            String[] c1 = mm[1].split(",");
    //            for (int i = 0; i < c.length; i++) {
    //                if (!c[i].equals(c1[i])) {
    //                    DLog.d(c[i] + " __> " + c1[i]);
    //                }
    //            }
    //            return mm[0] + "\n" + mm[1];
    //        }
    //        return replace;
    //    }
    fun i(tag: String?, msg: String) {
        if (DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun v(tag: String?, msg: String) {
        if (DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String?, msg: String) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun w(tag: String?, msg: String?, t: Throwable?) {
        if (DEBUG) {
            Log.w(tag, msg, t)
        }
    }

    fun w(tag: String?, msg: String) {
        if (DEBUG) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String?, msg: String) {
        if (DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun e(tag: String?, msg: String?, t: Throwable?) {
        if (DEBUG) {
            Log.e(tag, msg, t)
        }
    }
}
