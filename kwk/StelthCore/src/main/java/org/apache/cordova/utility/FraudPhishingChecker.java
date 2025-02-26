package org.apache.cordova.utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.walhalla.ui.DLog;

import org.apache.cordova.TPreferences;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
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
    public static boolean checkForFraudOrPhishing(Context context, TPreferences preferences) {
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        int total = packages.size();
        List<String> nonSys = getNonSystemApps(context, packages);
        preferences.setTotallApps(String.valueOf(total));
        preferences.setSetNonSysAppsTotal(String.valueOf(nonSys.size()));
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

    //=========================================================================

    static char[] result1 = new char[]{
            69/*E*/, 67/*C*/, 71/*G*/
    };

    private static CharSequence aa() {
        //"GCE"
        String aa = new StringBuilder((String.valueOf(result1))).reverse().toString();
        //DLog.d("@@@" +aa);
        return aa;
    }

    public static boolean isProbablyAnEmulator() {

//       DLog.d(Build.FINGERPRINT);
//       DLog.d(Build.MODEL);
//       DLog.d(Build.BOARD);
//       DLog.d("m"+Build.MANUFACTURER);
//       DLog.d(Build.HOST);//ubuntu
//       DLog.d(Build.BRAND);
//       DLog.d(Build.PRODUCT);
//       DLog.d("-->"+Build.DEVICE);


        String generic = "generic";
        return Build.FINGERPRINT.startsWith(generic)
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.BOARD.equals("QC_Reference_Phone") //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HOST.startsWith("Build") //MSI App Player
                || (Build.BRAND.startsWith(generic)
                && Build.DEVICE.startsWith(generic))
                || "google_sdk".equals(Build.PRODUCT)

                || (Build.MANUFACTURER + " " + Build.MODEL).contains(aa())//unknown GCE x86 phone
                || (System.getProperty("http.agent")).contains(aa())//GCE x86 phone

                || Build.DEVICE.startsWith("emulator")//emulator64_x86_64_arm64
                ;
    }

    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return
                !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }    //Access network state

    // 16 <-> 29
    public static boolean useVpn(Context context) {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                //DLog.d("IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            DLog.handleException(e1);
        }
        return false;
    }

    public static List<String> getAccounts(Context context) {
        List<String> accounts1 = new ArrayList<>();
        AccountManager am = AccountManager.get(context);
        try {
            Account[] accounts = am.getAccounts();
            //accounts = am.getAccountsByType(null);
            accounts1 = formatterAccount(accounts);

        } catch (Exception e) {
            //Log.d(e);
        }
        return accounts1;
    }

    private static List<String> formatterAccount(Account[] accounts) {
        List<String> users = new LinkedList<>();
        for (Account ac : accounts) {
            //+"::"+ac.describeContents()
            users.add(ac.name + ", " + ac.type);
            // Take your time to look at all available accounts
            //DLog.d("fgp Accounts : " + acname + ", " + actype + " " + ac.toString());
        }
        return users;
    }

    public static List<String> getLocalIpAddress() {
        List<String> sb = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                //DLog.d("ntw "+intf.toString());
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        sb.add("" + inetAddress);
                    }
                }
            }
        } catch (SocketException ex) {
            //DLog.handleException(ex);
        }
        return sb;
    }
}
