package com.walhalla.ui.plugins;

import static com.walhalla.ui.plugins.Launcher.openBrowser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.walhalla.abcsharedlib.Share;
import com.walhalla.ui.UConst;
import com.walhalla.ui.DLog;
import com.walhalla.ui.R;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Module_U {

    public static final String PKG_NAME_VENDING = "com.android.vending";
    private static final String E_AAB = "e-mail client not found";


    //public static final int REQUEST_CODE_SHARE_APP = 1878;


    //Show me the magik...

    private static boolean isFromGooglePlay(Context context, String pName) {
        PackageManager pm = context.getPackageManager();
        String name = pm.getInstallerPackageName(pName);
        // Installed from the Google Play
        if (name == null) {
            // Definitely not from Google Play
            return false;
        } else return PKG_NAME_VENDING.equals(name)
                || "com.google.android.feedback".equals(name);
    }

    public static boolean isFromGooglePlay(Context context) {
        return isFromGooglePlay(context, context.getPackageName());
    }

    public static void aboutDialog(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //&#169; - html
        String title = "\u00a9 " + year + " " + context.getString(R.string.play_google_pub);

        View mView = LayoutInflater.from(context).inflate(R.layout.about, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(null)
                .setCancelable(true)
                .setIcon(null)

                .setNegativeButton(R.string.action_discover_more_app, (dialog1, which) -> moreApp(context))
                .setPositiveButton(android.R.string.ok, null)

                .setView(mView)
                .create();
        mView.setOnClickListener(v -> dialog.dismiss());
        TextView textView = mView.findViewById(R.id.about_version);
        textView.setText(DLog.getAppVersion(context));
        TextView _c = mView.findViewById(R.id.about_copyright);
        _c.setText(title);
        ImageView logo = mView.findViewById(R.id.aboutLogo);
        logo.setOnLongClickListener(v -> {
            _c.setText(_o(context));
            return false;
        });
        //dialog.setButton();
        dialog.show();
    }

    private static String _o(Context context) {
        ApplicationInfo appInfo;
        int minSdk = -1;
        int targetSdk = -1;

        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                minSdk = appInfo.minSdkVersion;
            }
            targetSdk = appInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            DLog.handleException(e);
        }


        String _o = "[+]gp->" + isFromGooglePlay(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            _o = _o + ", category->" + context.getApplicationInfo().category;
        }
        _o = _o + ", "
                + "Device SDK: " + Build.VERSION.SDK_INT + "\n" +
                "minSdk: " + minSdk + "\n" +
                "targetSdk: " + targetSdk + "\n" +
                "compileSdk: Not available at runtime (compile-time value)";
        return _o;
    }


    public static Map<String, String> anomaly(Context context) {

        String current_package_name = "com.walhall.123";

        Map<String, String> map = new HashMap<>();
        ApplicationInfo info = context.getApplicationInfo();
        String processName = info.processName;
        String pn = context.getPackageName();
        if (!pn.equals(current_package_name)) {
            map.put("packageName", current_package_name + "::" + pn);
        }
        if (!pn.equals(processName)) {
            map.put("processName", processName + "::" + pn);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int category = info.category;
            if (category == -1) {
                map.put("category", "" + category);
            }
        }
        PackageManager pm = context.getPackageManager();
        String installer = pm.getInstallerPackageName(pn);
        map.put("installer", "" + installer);

        Signature[] sigs = new Signature[0];
        try {
            sigs = pm.getPackageInfo(pn, PackageManager.GET_SIGNATURES).signatures;
            if (sigs == null || sigs.length <= 0) {
            } else {
                for (Signature signature : sigs) {

                    X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signature.toByteArray()));
                    //DLog.d("@@123" + x509Certificate);
                    DLog.d("@@123" + x509Certificate.getSerialNumber().toString());//1231018131612
                    String[] mm = x509Certificate.getIssuerX500Principal().toString().split(", ");
                    for (int i = 0; i < mm.length; i++) {
                        String[] aaa = mm[i].trim().split("=");
                        if (aaa.length == 2) {
                            DLog.d(aaa[0] + "=>" + aaa[1]);
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            DLog.handleException(e);
        } catch (CertificateException e) {
            DLog.handleException(e);
        }
        return map;
    }

    /**
     * more_apps_link = "<a href="https://play.google.com/store/apps/dev?id=5700313618786177705">...</a>"
     */
    public static void moreApp(Context context) {
        final String pub = context.getString(R.string.play_google_pub);
        try {
            context.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + pub)));

        } catch (android.content.ActivityNotFoundException anfe) {
            openBrowser(context, "https://play.google.com/store/search?q=pub:" + pub);
        }
    }


    public static void feedback(Context context) {

        try {
            String packageName = context.getPackageName();
            String result;
            String[] parts = packageName.split("\\.", 3); // Разбиваем строку по точкам, ограничивая разделение только первыми двумя точками
            if (parts.length >= 3) {
                result = parts[2]; // Берем третью часть (все после второй точки)
            } else {
                result = packageName;
            }
            String subject = Uri.encode(result) + "_" + DLog.getAppVersion(context);
            //DLog.d(subject + "\t" + context.getString(R.string.publisher_feedback_email));


//            intent.setData(Uri.parse("mailto:" + PublisherConfig.FEEDBACK_EMAIL +
//                    "?share_subject=" + Uri.encode(context.getPackageName())));

            composeEmail(context, new String[]{context.getString(R.string.publisher_feedback_email)}, subject);
        } catch (Exception e) {
            DLog.handleException(e);
            Toast.makeText(context, E_AAB, Toast.LENGTH_LONG).show();
        }
    }

    private static void composeEmail(Context context, String[] addresses, String subject) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, E_AAB, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            DLog.handleException(e);
            Toast.makeText(context, E_AAB, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Open in Other app or Cope to buffer
     *
     * @param context
     * @param extra   extra.length
     *                49199 - ok
     *                97309 - not work, remove intent.putExtra(comPinterestEXTRA_DESCRIPTION, extra);
     */
    public static void shareText(Context context, String extra, String chooserTitle) {
        DLog.d("{share} " + extra.length());
        if (chooserTitle == null) {
            chooserTitle = context.getResources().getString(R.string.app_name);
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, extra);
        intent.setType(MimeType.TEXT_PLAIN);


        //intent.putExtra(Intent.EXTRA_EMAIL, "alexvarboffin@gmail.com");//Work only with intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        //intent.setType("*/*");
        if (extra.length() < 50000) {
            intent.putExtra(Share.comPinterestEXTRA_DESCRIPTION, extra);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooser = Intent.createChooser(intent, chooserTitle);
        //E/DatabaseUtils: Writing exception to parcel
        //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider uri content:
        //Android 11
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (!resInfoList.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                String activityName = resolveInfo.activityInfo.name;
                DLog.d("[" + packageName + "]" + activityName);
                //context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            DLog.d("Not found activity...");
        }
        chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        try {
            context.startActivity(chooser);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    public static void shareThisApp(Context context, @Nullable String message) {
        if (message == null) {
            message = context.getString(R.string.share_text_default)
                    + (char) 10 + UConst.GOOGLE_PLAY_CONSTANT
                    + context.getPackageName() + (char) 10;
        }


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(MimeType.TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        //no need => intent.putExtra("ru.ok.android.action.SEND_MESSAGE", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //v1
        context.startActivity(intent);

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

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {

//            NetworkInfo info0 = cm.getActiveNetworkInfo();
//            boolean c1 = info0 != null && info0.isAvailable() && info0.isConnected();

                NetworkInfo[] info = cm.getAllNetworkInfo();
                for (NetworkInfo networkInfo : info) {
                    boolean c0 = networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.isConnected();
                    if (c0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }
        return false;
    }

    public static void shareThisApp(Context context) {
        shareThisApp(context, null);
    }

    public static void actionWirelessSettings(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DLog.handleException(e);
        }
    }
}