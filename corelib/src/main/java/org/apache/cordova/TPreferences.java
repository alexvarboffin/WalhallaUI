package org.apache.cordova;

import static org.apache.cordova.Const.PREF_KEY_FBM_TKN;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;

import org.apache.P;

import org.apache.Utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TPreferences {

    private static TPreferences instance;
    private final android.content.SharedPreferences preferences;
    private static final String EMPTY_DATA = "";

    public static final String KEY_MUTED = "key_muted_0";
    private static final String KEY_T_URL = "k_urla";

    private static final String aaa_f_k = "key_first000900";

    private TPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public synchronized static TPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new TPreferences(context);
        }

        return instance;
    }

//    public android.content.SharedPreferences sharedPreferences() {
//        return preferences;
//    }

    /**
     * Lock webview
     */

    public void appWebDisabler(boolean data) {
        preferences.edit().putString(KEY_MUTED, String.valueOf(data)).apply();
    }

    //Emulator Timezone 0
    public boolean appWebDisabler(Context context) {

//        DLog.d("[L] => " + Utils.timeZone());
//        DLog.d("[L] => " + Build.MANUFACTURER + Build.MODEL);
//        DLog.d("[L] => " + Chipper.pixel(Build.MANUFACTURER + Build.MODEL));


        List<Boolean> muter = new ArrayList<>();
//        muter.add(
//                Chipper.pixel(Build.MANUFACTURER + Build.MODEL)
//                        && Utils.timeZone() == 0
//        );
        //@@ muter.add(!Utils.isSimSupport(context));//Lock Without sim-card

        //muter.add((Build.MANUFACTURER + Build.MODEL).contains("Googlesdk_gphone_x86"));
        //muter.add("en".equals(context.getString(R.string.app_locale)));
        //muter.add("en".equals(context.getString(R.string.app_locale)));
//++        muter.add(Utils.timeZone() == 0);
        //muter.add(Utils.timeZone() == 3);


//        muter.add(Utils.useVpn(context));//If use locked


        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> nonSys = Chipper.getAllApps(context, packages);
        muter.add(nonSys.size() < 3);


        String muted = preferences.getString(KEY_MUTED, null);
        if (muted == null) { //App first launch
            boolean a = false;
            for (boolean entity : muter) {
                if (entity) {
                    a = entity;
                }
            }

            //no muted=>
            preferences.edit().putString(KEY_MUTED, String.valueOf(a)).apply();
            muted = String.valueOf(a);
        }
        DLog.d("muted->" + muted);
        return Boolean.parseBoolean(muted);
    }


    public String locale() {
        return preferences.getString(String.valueOf((char) 77),
                Locale.getDefault().toString().toLowerCase());
    }

    public void userId(String uid) {
        preferences.edit().putString(P.KEY_U_ID, uid).apply();
    }

    public String userId() {
        return preferences.getString(P.KEY_U_ID, "None");
    }

    public boolean noneFirst() {
        return preferences.getBoolean(aaa_f_k, false);
    }

    public void noneFirstEnable() {
        preferences.edit().putBoolean(aaa_f_k, true).apply();
    }

//    public void locale(boolean b) {
//        app_prefs.edit().putString(String.valueOf((char)77), b).apply();
//    }

    public static String getFirebaseToken(Context context) {
        android.content.SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return preferences.getString(PREF_KEY_FBM_TKN, EMPTY_DATA);
    }

    public void paramSetter(String name, String value) {
        preferences.edit().putString(name, value).apply();
        DLog.d("[P] => [" + name + "] [" + value + "]");
    }

    public String getTargetUrl() {
        return preferences.getString(KEY_T_URL, null);
    }

    public void setTargetUrl(String url) {
        preferences.edit().putString(KEY_T_URL, url).apply();
    }

    public void setMute(String aTrue) {
        preferences.edit().putString(KEY_MUTED, aTrue).apply();
    }
}
