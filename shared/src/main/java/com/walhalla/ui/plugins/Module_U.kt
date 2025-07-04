package com.walhalla.ui.plugins

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.walhalla.shared.R
import com.walhalla.ui.DLog

import java.io.ByteArrayInputStream
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Calendar
import androidx.core.net.toUri

object Module_U {
    const val PKG_NAME_VENDING: String = "com.android.vending"
    private const val E_AAB = "e-mail client not found"


    //public static final int REQUEST_CODE_SHARE_APP = 1878;
    //Show me the magik...
    private fun isFromGooglePlay(context: Context, pName: String): Boolean {
        val pm = context.packageManager
        val name = pm.getInstallerPackageName(pName)
        // Installed from the Google Play
        return if (name == null) {
            // Definitely not from Google Play
            false
        } else PKG_NAME_VENDING == name
                || "com.google.android.feedback" == name
    }

    fun isFromGooglePlay(context: Context): Boolean {
        return isFromGooglePlay(context, context.packageName)
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


    fun anomaly(context: Context): Map<String, String> {
        val current_package_name = "com.walhall.123"

        val map: MutableMap<String, String> = HashMap()
        val info = context.applicationInfo
        val processName = info.processName
        val pn = context.packageName
        if (pn != current_package_name) {
            map["packageName"] = "$current_package_name::$pn"
        }
        if (pn != processName) {
            map["processName"] = "$processName::$pn"
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val category = info.category
            if (category == -1) {
                map["category"] = "" + category
            }
        }
        val pm = context.packageManager
        val installer = pm.getInstallerPackageName(pn)
        map["installer"] = "" + installer

        var sigs: Array<Signature?>? = arrayOfNulls(0)
        try {
            sigs = pm.getPackageInfo(pn, PackageManager.GET_SIGNATURES).signatures
            if (sigs == null || sigs.size <= 0) {
            } else {
                for (signature in sigs) {
                    val x509Certificate =
                        CertificateFactory.getInstance("X.509").generateCertificate(
                            ByteArrayInputStream(signature!!.toByteArray())
                        ) as X509Certificate
                    //DLog.d("@@123" + x509Certificate);
                    DLog.d("@@123" + x509Certificate.serialNumber.toString()) //1231018131612
                    val mm = x509Certificate.issuerX500Principal.toString().split(", ".toRegex())
                        .dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (i in mm.indices) {
                        val aaa = mm[i].trim { it <= ' ' }.split("=".toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (aaa.size == 2) {
                            DLog.d(aaa[0] + "=>" + aaa[1])
                        }
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            DLog.handleException(e)
        } catch (e: CertificateException) {
            DLog.handleException(e)
        }
        return map
    }

    /**
     * more_apps_link = "[...](https://play.google.com/store/apps/dev?id=5700313618786177705)"
     */
    @JvmStatic
    fun moreApp(context: Context) {
        val pub = context.getString(R.string.play_google_pub)
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "market://search?q=pub:$pub".toUri())
            )
        } catch (anfe: ActivityNotFoundException) {
            Launcher.openBrowser(
                context,
                "https://play.google.com/store/search?q=pub:$pub"
            )
        }
    }

    @JvmStatic
    fun feedback(context: Context) {
        try {
            val packageName = context.packageName
            val result: String
            val parts = packageName.split("\\.".toRegex(), limit = 3)
                .toTypedArray() // Разбиваем строку по точкам, ограничивая разделение только первыми двумя точками
            result = if (parts.size >= 3) {
                parts[2] // Берем третью часть (все после второй точки)
            } else {
                packageName
            }
            val subject = Uri.encode(result) + "_" + DLog.getAppVersion(context)


            //DLog.d(subject + "\t" + context.getString(R.string.publisher_feedback_email));


//            intent.setData(Uri.parse("mailto:" + PublisherConfig.FEEDBACK_EMAIL +
//                    "?share_subject=" + Uri.encode(context.getPackageName())));
            composeEmail(
                context,
                arrayOf(context.getString(R.string.publisher_feedback_email)),
                subject
            )
        } catch (e: Exception) {
            DLog.handleException(e)
            Toast.makeText(context, E_AAB, Toast.LENGTH_LONG).show()
        }
    }

    private fun composeEmail(context: Context, addresses: Array<String>, subject: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setData("mailto:".toUri()) // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, "")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, E_AAB, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            DLog.handleException(e)
            Toast.makeText(context, E_AAB, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Open in Other app or Cope to buffer
     *
     * @param context
     * @param extra   extra.length
     * 49199 - ok
     * 97309 - not work, remove intent.putExtra(comPinterestEXTRA_DESCRIPTION, extra);
     */
    fun shareText(context: Context, extra: String, chooserTitle: String?) {
        var chooserTitle = chooserTitle
        DLog.d("{share} " + extra.length)
        if (chooserTitle == null) {
            chooserTitle = context.resources.getString(R.string.app_name)
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, extra)
        intent.setType(MimeType.TEXT_PLAIN)


        //intent.putExtra(Intent.EXTRA_EMAIL, "alexvarboffin@gmail.com");//Work only with intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
        //intent.setType("*/*");
        if (extra.length < 50000) {
            intent.putExtra(com.walhalla.abcsharedlib.Share.comPinterestEXTRA_DESCRIPTION, extra)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val chooser = Intent.createChooser(intent, chooserTitle)
        //E/DatabaseUtils: Writing exception to parcel
        //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider uri content:
        //Android 11
        val resInfoList =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (!resInfoList.isEmpty()) {
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                val activityName = resolveInfo.activityInfo.name
                DLog.d("[$packageName]$activityName")
                //context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            DLog.d("Not found activity...")
        }
        chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        try {
            context.startActivity(chooser)
        } catch (e: Exception) {
            DLog.handleException(e)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun shareThisApp(context: Context, message: String? = null) {
        var message = message
        if (message == null) {
            message = (context.getString(R.string.share_text_default)
                    + 10.toChar() + com.walhalla.ui.UConst.GOOGLE_PLAY_CONSTANT
                    + context.packageName + 10.toChar())
        }


        val intent = Intent(Intent.ACTION_SEND)
        intent.setType(MimeType.TEXT_PLAIN)
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        //no need => intent.putExtra("ru.ok.android.action.SEND_MESSAGE", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //v1
        context.startActivity(intent)

        //v2
//        Intent sender = Intent.createChooser(intent, "Share " + context.getString(R.string.app_name));
//        context.startActivity(sender);
    }

    //    public static void checkUpdate(Context context) {
    //        AppUpdater updater = new AppUpdater(context)
    //                .setContentOnUpdateAvailable(R.string.update_available)
    //                .setCancelable(false)
    //                .setButtonDoNotShowAgain("")
    //                .setButtonUpdate(R.string.update_now)
    //                .setButtonDismiss(R.string.update_later)
    //                .setTitleOnUpdateNotAvailable(R.string.update_not_available)
    //                .setContentOnUpdateNotAvailable(R.string.update_check_later);
    //        updater.start();
    //    }
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
//            NetworkInfo info0 = cm.getActiveNetworkInfo();
//            boolean c1 = info0 != null && info0.isAvailable() && info0.isConnected();
                val info = cm.allNetworkInfo
                for (networkInfo in info) {
                    val c0 =
                        networkInfo.state == NetworkInfo.State.CONNECTED || networkInfo.isConnected
                    if (c0) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        }
        return false
    }

    fun actionWirelessSettings(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            DLog.handleException(e)
        }
    }
}