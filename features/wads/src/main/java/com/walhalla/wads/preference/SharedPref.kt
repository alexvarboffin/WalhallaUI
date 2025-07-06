package com.walhalla.wads.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit

class SharedPref private constructor(activity: Context) {
    private val settings: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(activity)

    val isBannerAlreadyShown: Boolean
        //========================== Session Manager =============================
        get() = settings.getBoolean(
            BANNER_SHOWN_KEY,
            false
        )

    fun setBannerShown() {
        settings.edit { putBoolean(BANNER_SHOWN_KEY, true) }
    }

    fun resetBannerShown() {
        settings.edit { putBoolean(BANNER_SHOWN_KEY, false) }
    }

    var launchCount: Int
        get() = settings.getInt(
            LAUNCH_COUNT_KEY,
            0
        )
        set(launchCount) {
            settings.edit {
                putInt(
                    LAUNCH_COUNT_KEY,
                    launchCount
                )
            }
        }

    private fun incrementLaunchCount() {
        val launchCount = settings.getInt(LAUNCH_COUNT_KEY, 0) + 1
        settings.edit {
            putInt(LAUNCH_COUNT_KEY, launchCount)
        }
    }

    companion object {
        private const val LAUNCH_COUNT_KEY = "var12300"
        private const val BANNER_SHOWN_KEY = "banner_shown"
        private var instance: SharedPref? = null

        @Synchronized
        fun getInstance(context: Context): SharedPref {
            if (instance == null) {
                instance = SharedPref(context)
            }
            return instance!!
        }
    }
}
