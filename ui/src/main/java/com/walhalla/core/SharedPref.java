package com.walhalla.core;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPref {
    private static final String BANNER_SHOWN_KEY = "banner_shown";

    private static final String KEY_AGREE = "licenseagreeok";
    private static final String KEY_RATED = "key_rate_not_show_again";

    //private static final String KEY_FIRST_LAUNCH = "sp.fr.launch";
    private static final String LAUNCH_COUNT_KEY = "var123";


    private static final String KEY_RELOADED = "rate_launch_count";
    private static final String DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH_";
    private static SharedPref instance;


    private final SharedPreferences settings;


    public static synchronized SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context);
        }
        return instance;
    }

    private SharedPref(Context activity) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public boolean licenseAgree() {
        return settings.getBoolean(KEY_AGREE, false);
    }


    public void licenseAgree(boolean b) {
        settings.edit().putBoolean(KEY_AGREE, b).apply();
    }

    //Rate app
    public void appResumeCount(int count) {
        settings.edit().putInt(KEY_RELOADED, count).apply();
    }

    public int appResumeCount() {
        return settings.getInt(KEY_RELOADED, 0);
    }

    public boolean appRated() {
        boolean flg = settings.getBoolean(KEY_RATED, false);
//        if (BuildConfig.DEBUG) {
//            DLog.d("[?] Rated -> " + flg
//                    + " " + appReloadedCount() + "/" + LAUNCHES_UNTIL_PROMPT
//                    + " " + date_firstLaunch());
//        }
        return flg;
    }

    public void appRated(boolean rated) {
        settings.edit().putBoolean(KEY_RATED, rated).apply();
    }

    public void date_firstLaunch(long date_firstLaunch) {
        settings.edit().putLong(DATE_FIRST_LAUNCH, date_firstLaunch).apply();
    }

    public long date_firstLaunch() {
        return settings.getLong(DATE_FIRST_LAUNCH, 0);
    }

    public int getLaunchCount() {
        return settings.getInt(LAUNCH_COUNT_KEY, 0);
    }

    public void setLaunchCount(int launchCount) {
        settings.edit().putInt(LAUNCH_COUNT_KEY, launchCount).apply();
    }

    private void incrementLaunchCount() {
        int launchCount = settings.getInt(LAUNCH_COUNT_KEY, 0) + 1;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(LAUNCH_COUNT_KEY, launchCount);
        editor.apply();
    }


    //========================== Session Manager =============================
    public boolean isBannerAlreadyShown() {
        return settings.getBoolean(BANNER_SHOWN_KEY, false);
    }

    public void setBannerShown() {
        settings.edit().putBoolean(BANNER_SHOWN_KEY, true).apply();
    }

    public void resetBannerShown() {
        settings.edit().putBoolean(BANNER_SHOWN_KEY, false).apply();
    }
}
