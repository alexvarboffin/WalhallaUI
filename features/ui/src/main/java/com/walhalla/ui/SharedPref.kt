package com.walhalla.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPref private constructor(activity: Context) {
    private val settings: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(activity)

    fun licenseAgree(): Boolean {
        return settings.getBoolean(KEY_AGREE, false)
    }


    fun licenseAgree(b: Boolean) {
        settings.edit().putBoolean(KEY_AGREE, b).apply()
    }

    //Rate app
    fun appResumeCount(count: Int) {
        settings.edit().putInt(KEY_RELOADED, count).apply()
    }

    fun appResumeCount(): Int {
        return settings.getInt(KEY_RELOADED, 0)
    }

    fun appRated(): Boolean {
        val flg = settings.getBoolean(KEY_RATED, false)
        //        if (BuildConfig.DEBUG) {
//            DLog.d("[?] Rated -> " + flg
//                    + " " + appReloadedCount() + "/" + LAUNCHES_UNTIL_PROMPT
//                    + " " + date_firstLaunch());
//        }
        return flg
    }

    fun appRated(rated: Boolean) {
        settings.edit().putBoolean(KEY_RATED, rated).apply()
    }

    fun date_firstLaunch(date_firstLaunch: Long) {
        settings.edit().putLong(DATE_FIRST_LAUNCH, date_firstLaunch).apply()
    }

    fun date_firstLaunch(): Long {
        return settings.getLong(DATE_FIRST_LAUNCH, 0)
    }

    var launchCount: Int
        get() = settings.getInt(LAUNCH_COUNT_KEY, 0)
        set(launchCount) {
            settings.edit()
                .putInt(LAUNCH_COUNT_KEY, launchCount).apply()
        }

    private fun incrementLaunchCount() {
        val launchCount = settings.getInt(LAUNCH_COUNT_KEY, 0) + 1
        val editor = settings.edit()
        editor.putInt(LAUNCH_COUNT_KEY, launchCount)
        editor.apply()
    }

    companion object {
        private const val KEY_AGREE = "licenseagreeok"
        private const val KEY_RATED = "key_rate_not_show_again"

        //private static final String KEY_FIRST_LAUNCH = "sp.fr.launch";
        private const val LAUNCH_COUNT_KEY = "var_launch"


        private const val KEY_RELOADED = "rate_launch_count"
        private const val DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH_"
        private var instance: SharedPref? = null


        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): SharedPref {
            if (instance == null) {
                instance = SharedPref(context)
            }
            return instance!!
        }
    }
}
