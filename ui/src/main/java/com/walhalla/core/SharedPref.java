package com.walhalla.core;

import static com.walhalla.ui.BuildConfig.DEBUG;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;

public class SharedPref {

    private static final String KEY_AGREE = "license_agree_ok";
    private static final String KEY_RATED = "key_rate_not_show_again";

    private static final String KEY_FIRST_LAUNCH = "sp.fr.launch";
    private static final String KEY_RELOADED = "rate_launch_count";
    private static final String DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH_";

    private final SharedPreferences mSharedPreferences;

    public SharedPref(Context activity) {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }


    public boolean licenseAgree() {
        return mSharedPreferences.getBoolean(KEY_AGREE, false);
    }


    public void licenseAgree(boolean b) {
        mSharedPreferences.edit().putBoolean(KEY_AGREE, b).apply();
    }

    //Rate app
    public void appResumeCount(int count) {
        mSharedPreferences.edit().putInt(KEY_RELOADED, count).apply();
    }

    public int appResumeCount() {
        return mSharedPreferences.getInt(KEY_RELOADED, 0);
    }

    public boolean appRated() {
        boolean flg = mSharedPreferences.getBoolean(KEY_RATED, false);
//        if (DEBUG) {
//            DLog.d("[?] Rated -> " + flg
//                    + " " + appReloadedCount() + "/" + LAUNCHES_UNTIL_PROMPT
//                    + " " + date_firstLaunch());
//        }
        return flg;
    }

    public void appRated(boolean rated) {
        mSharedPreferences.edit().putBoolean(KEY_RATED, rated).apply();
    }

    public void date_firstLaunch(long date_firstLaunch) {
        mSharedPreferences.edit().putLong(DATE_FIRST_LAUNCH, date_firstLaunch).apply();
    }

    public long date_firstLaunch() {
        return mSharedPreferences.getLong(DATE_FIRST_LAUNCH, 0);
    }

    public int startCount() {
        return mSharedPreferences.getInt(KEY_FIRST_LAUNCH, 0);
    }

    public void startCount(int i) {
        mSharedPreferences.edit().putInt(KEY_FIRST_LAUNCH, i).apply();
    }
}
