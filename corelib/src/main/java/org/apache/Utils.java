package org.apache;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import com.walhalla.ui.DLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class Utils {


    public static String getBrand() {
        return Build.BRAND;//.toUpperCase();
    }

    public static String getDeviceModel() {
        return Build.MODEL;//.toUpperCase();
    }


    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return
                !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }

    public static String getCountryCode(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String c = "";
        if (tm != null) {
            c = tm.getSimCountryIso().toUpperCase();
//            DLog.d("fgp " + c + " " + tm.getNetworkCountryIso().toUpperCase() + " "
//                    + Locale.getDefault().getISO3Country().toUpperCase()
//                    + " " + Locale.getDefault().getCountry().toUpperCase()
//                    + " " + context.getResources().getConfiguration().locale.getISO3Country());

            if (c.isEmpty()) {
                c = tm.getNetworkCountryIso().toUpperCase();
            }
        }
        if (c.isEmpty()) {
            c = Locale.getDefault().getISO3Country().toUpperCase();
        }
        if (c.isEmpty()) {
            c = Locale.getDefault().getCountry().toUpperCase();
        }
        if (c.isEmpty()) {
            c = context.getResources().getConfiguration().locale.getISO3Country();
        }
        return c;
    }


//    public static boolean isNetworkConnected(Context context) {
//        ConnectivityManager manager = ((ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        NetworkInfo activeNetwork = (manager == null) ? null : manager.getActiveNetworkInfo();
//        return (activeNetwork != null
//                //&& activeNetwork.isConnectedOrConnecting(); //Variant #1
//                && manager.getActiveNetworkInfo().isAvailable()
//                && manager.getActiveNetworkInfo().isConnected()
//        );
//    }

    public static String appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);
        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;
        }
        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());
        return newUri.toString();
    }

    public static long timeZone() {
        return TimeUnit.HOURS.convert(TimeZone.getDefault().getRawOffset(), TimeUnit.MILLISECONDS);
        /*TimeZone.getDefault().getDisplayName()TimeZone.getDefault().getRawOffset()localTime*/
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


        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.BOARD.equals("QC_Reference_Phone") //bluestacks
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HOST.startsWith("Build") //MSI App Player
                || (Build.BRAND.startsWith("generic")
                && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)

                || (Build.MANUFACTURER + " " + Build.MODEL).contains(aa())//unknown GCE x86 phone
                || (System.getProperty("http.agent")).contains(aa())//GCE x86 phone

                ||Build.DEVICE.startsWith("emulator")//emulator64_x86_64_arm64
        ;
    }

    static char[] result1 = new char[]{
            69/*E*/, 67/*C*/, 71/*G*/
    };

    private static CharSequence aa() {
        //"GCE"
        String aa = new StringBuilder((String.valueOf(result1))).reverse().toString();
        //DLog.d("@@@" +aa);
        return aa;
    }

    public static void hideKeyboard(@NonNull Activity a) {
        try {
            InputMethodManager imm = (InputMethodManager) a.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = a.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(a);
            }
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
    }


    //Access network state
    // 16 <-> 29
    public static boolean useVpn(Context context) {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                DLog.d("IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return false;
    }


    public static String makeDate() {
        String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        //DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        //return dateFormat.format(new Date());
        return simpleDateFormat.format(new Date());
    }

    public static Boolean isNetworkAvailable(Context application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }

    //        Don't use System.getProperty("http.agent") - this will return the 'Dalvik'
    //        user agent (Dalvik is the VM that individual Android apps run within)




    public static void snack(Activity context, String msg) {
        View view = context.getWindow().getDecorView();
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAction(android.R.string.ok, null).show();
    }

    public static boolean isDeviceRooted() {
        // Проверяем наличие стандартных файлов и каталогов, которые могут быть изменены только с рут-правами
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        // Проверяем наличие библиотек, которые могут быть загружены только с рут-правами
        try {
            Process process = Runtime.getRuntime().exec(new String[] {"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                return true;
            }
        } catch (Exception e) {
            // игнорируем исключение
        }
        // Если проверки не выявили наличие рут-прав, возвращаем false
        return false;
    }

    public static boolean isAppSignatureValid(Context context) {
        return true;
    }
}
