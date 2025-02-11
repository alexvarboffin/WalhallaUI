package com.walhalla.wads.preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPref {
    
    private static final String LAUNCH_COUNT_KEY = "var12300";
    private static final String BANNER_SHOWN_KEY = "banner_shown";
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
}
