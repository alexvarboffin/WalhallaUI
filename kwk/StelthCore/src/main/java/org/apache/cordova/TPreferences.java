package org.apache.cordova;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import org.apache.cordova.generated.CheckTypeKey0;
import org.apache.cordova.generated.P;
import org.apache.cordova.utility.FraudPhishingChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TPreferences {

    public static final String TAG = "@@@";
    public static final String PREF_KEY_IS_FROM_PUSH = "key_is_from_push"; //is_from_push=true - если приложение открыто с пуша


    private static TPreferences instance;

    public SharedPreferences getPreferences() {
        return preferences;
    }

    private final SharedPreferences preferences;
    private static final String EMPTY_DATA = "";

    public static final String KEY_MUTED_ = "key_muted";
    private static final String KEY_T_URL = "k_urlaa";

    private static final String aaa_f_k = "key_firstjjj000900";

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
        preferences.edit().putString(KEY_MUTED_, String.valueOf(data)).apply();
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


        //Splash
        //muter.add(FraudPhishingChecker.checkForFraudOrPhishing(context));
        return fraudChecker(muter);
    }

    public boolean fraudChecker(List<Boolean> muter) {
        String muted = preferences.getString(KEY_MUTED_, null);
        if (muted == null) { //App first launch
            boolean a = false;
            for (boolean entity : muter) {
                if (entity) {
                    a = entity;
                }
            }
            //no muted=>
            preferences.edit().putString(KEY_MUTED_, String.valueOf(a)).apply();
            muted = String.valueOf(a);
        }
        Log.d(TAG, "muted->" + muted);
        return Boolean.parseBoolean(muted);
    }


    public void fraudCheck(Context context, String[] checkas) {
        for (int i = 0; i < checkas.length; i++) {
            String check = checkas[i];
            String m = preferences.getString(check, null);
            if (m == null) {
                Boolean result = null;
                if (CheckTypeKey0.CH_ISPROBABLYANEMULATOR.equals(check)) {
                    result = FraudPhishingChecker.isProbablyAnEmulator();
                } else if (CheckTypeKey0.CH_FORFRAUDORPHISHING.equals(check)) {
                    result = FraudPhishingChecker.checkForFraudOrPhishing(context, this);
                } else if (CheckTypeKey0.CH_ISSIMSUPPORT.equals(check)) {
                    result = FraudPhishingChecker.isSimSupport(context);
                } else if (CheckTypeKey0.CH_USEVPN.equals(check)) {
                    result = FraudPhishingChecker.useVpn(context);
                }else if (CheckTypeKey0.CH_GETLOCALIPADDRESS.equals(check)) {
                    List<String> result0 = FraudPhishingChecker.getLocalIpAddress();
                    setLocalIpAddress(String.valueOf(result0));
                    continue;
                }
                if (result != null) {
                    preferences.edit().putString(check, String.valueOf(result)).apply();
                }
            }
        }
    }

    public String getAllFraudCheck0() {
        StringBuilder sb = new StringBuilder();
        for (String s : CheckTypeKey0.allCheck) {
            String muted = preferences.getString(s, null);
            sb.append(s).append("=>").append(muted).append((char) 10);
        }
        return sb.toString();
    }

    public boolean getFraudCheck(String check) {
        String muted = preferences.getString(check, null);
        return muted == null;
    }

    public String locale() {
        return preferences.getString(String.valueOf((char) 77),
                Locale.getDefault().toString().toLowerCase());
    }

    public void userId(String uid) {
        preferences.edit().putString(P.PREF_KEY_USR_ID, uid).apply();
    }

    public String userId() {
        return preferences.getString(P.PREF_KEY_USR_ID, "None");
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
        return preferences.getString(Cst.PREF_KEY_FBM_TKN, EMPTY_DATA);
    }

    public void paramSetter(String name, String value) {
        preferences.edit().putString(name, value).apply();
        Log.d(TAG, "[P] => [" + name + "] [" + value + "]");
    }

    public String getTargetUrl() {
        return preferences.getString(KEY_T_URL, null);
    }

    public void setTargetUrl(String url) {
        preferences.edit().putString(KEY_T_URL, url).apply();
    }

    public void setMute(String aTrue) {
        preferences.edit().putString(KEY_MUTED_, aTrue).apply();
    }

    public boolean noneMuted() {
        String muted = preferences.getString(KEY_MUTED_, null);
        return muted == null;
    }



    //==============================================================================
    public void setAccounts(String accounts) {
        preferences.edit().putString(CheckKey.CH_ACCOUNTS, accounts).apply();
    }

    public String getAccounts() {
        return preferences.getString(CheckKey.CH_ACCOUNTS, null);
    }

    public void setTotallApps(String TotallApps) {
        preferences.edit().putString(CheckKey.CH_TOTALLAPPS, TotallApps).apply();
    }

    public String getTotallApps() {
        return preferences.getString(CheckKey.CH_TOTALLAPPS, null);
    }

    public void setSetNonSysAppsTotal(String setNonSysAppsTotal) {
        preferences.edit().putString(CheckKey.CH_SETNONSYSAPPSTOTAL, setNonSysAppsTotal).apply();
    }

    public String getSetNonSysAppsTotal() {
        return preferences.getString(CheckKey.CH_SETNONSYSAPPSTOTAL, null);
    }

    public void setSimSupport(String SimSupport) {
        preferences.edit().putString(CheckKey.CH_SIMSUPPORT, SimSupport).apply();
    }

    public String getSimSupport() {
        return preferences.getString(CheckKey.CH_SIMSUPPORT, null);
    }

    public void setVpn(String vpn) {
        preferences.edit().putString(CheckKey.CH_VPN, vpn).apply();
    }

    public String getVpn() {
        return preferences.getString(CheckKey.CH_VPN, null);
    }

    public void setDeeplink(String deeplink) {
        preferences.edit().putString(CheckKey.CH_DEEPLINK, deeplink).apply();
    }

    public String getDeeplink() {
        return preferences.getString(CheckKey.CH_DEEPLINK, null);
    }

    public void setReferrer(String referrer) {
        preferences.edit().putString(CheckKey.CH_REFERRER, referrer).apply();
    }

    public String getReferrer() {
        return preferences.getString(CheckKey.CH_REFERRER, null);
    }

    public void setUtm_medium(String utm_medium) {
        preferences.edit().putString(CheckKey.CH_UTM_MEDIUM, utm_medium).apply();
    }

    public String getUtm_medium() {
        return preferences.getString(CheckKey.CH_UTM_MEDIUM, null);
    }

    public void setLocalIpAddress(String LocalIpAddress) {
        preferences.edit().putString(CheckKey.CH_LOCALIPADDRESS, LocalIpAddress).apply();
    }

    public String getLocalIpAddress() {
        return preferences.getString(CheckKey.CH_LOCALIPADDRESS, null);
    }

    public void setAdvertisingId(String advertisingId) {
        preferences.edit().putString(CheckKey.CH_ADVERTISINGID, advertisingId).apply();
    }

    public String getAdvertisingId() {
        return preferences.getString(CheckKey.CH_ADVERTISINGID, null);
    }



    public static class CheckKey {
        public static final String CH_ACCOUNTS = "itgR7gVB6z8WKiFwqsBU0g==";
        public static final String CH_TOTALLAPPS = "SZaa2FdyyYCzucSqn7wgLg==";
        public static final String CH_SETNONSYSAPPSTOTAL = "xSozvCi3WplS5o1/d8ZdKQ==";
        public static final String CH_SIMSUPPORT = "Qohn92FdwmsYa+MwgEZQXA==";
        public static final String CH_VPN = "IYmuXe4uS6UaWXqDNYB8uA==";
        public static final String CH_DEEPLINK = "HqZZ+dfpYHscanRCOB1G7A==";
        public static final String CH_REFERRER = "p/IwRV0GJs/XsKPGN6kAzA==";
        public static final String CH_UTM_MEDIUM = "6Zfhd/PTxLSAEyaDp9F0Zg==";
        public static final String CH_LOCALIPADDRESS = "M3tgbQ4MBbepO6Po1oG3xw==";
        public static final String CH_ADVERTISINGID = "tDLBc89Xwb4uE9l8drCTWg==";
    }


    //==============================================================================

    public boolean isFromPushLaunch() {
        return preferences.getBoolean(PREF_KEY_IS_FROM_PUSH, false);
    }
}
