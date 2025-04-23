package com.androidsx.rateme

import android.content.Context
import android.os.Bundle
import android.util.Log
import java.util.Date
import androidx.core.content.edit

/**
 * Timer to schedule the rate-me after a number of application launches.
 */
object RateMeDialogTimer {
    private val TAG: String = RateMeDialogTimer::class.java.simpleName

    private const val PREF_NAME = "RateThisApp"
    private const val KEY_INSTALL_DATE = "rta_install_date"
    private const val KEY_LAUNCH_TIMES = "rta_launch_times"
    private const val KEY_OPT_OUT = "rta_opt_out"

    private var mInstallDate = Date()
    private var mLaunchTimes = 0
    private var mOptOut = false

    fun onStart(context: Context, savedInstanceState: Bundle?) {
        // Only use FIRST launch of the activity
        if (savedInstanceState != null) {
            return
        }
        saveInPreferences(context)
    }

    fun onStart(context: Context) {
        saveInPreferences(context)
    }

    private fun saveInPreferences(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            // If it is the first launch, save the date in shared preference.
            if (pref.getLong(KEY_INSTALL_DATE, 0) == 0L) {
                val now = Date()
                putLong(KEY_INSTALL_DATE, now.time)
                Log.d(TAG, "First install: $now")
            }
            // Increment launch times
            var launchTimes = pref.getInt(KEY_LAUNCH_TIMES, 0)
            launchTimes++
            putInt(KEY_LAUNCH_TIMES, launchTimes)
            Log.d(TAG, "Launch times; $launchTimes")
        }

        mInstallDate = Date(pref.getLong(KEY_INSTALL_DATE, 0))
        mLaunchTimes = pref.getInt(KEY_LAUNCH_TIMES, 0)
        mOptOut = pref.getBoolean(KEY_OPT_OUT, false)
    }

    fun shouldShowRateDialog(context: Context, installDays: Int, launchTimes: Int): Boolean {
        if (mOptOut) {
            return false
        } else {
            if (mLaunchTimes >= launchTimes) {
                clearSharedPreferences(context)
                return true
            }
            val thresholdMillis = installDays * 24 * 60 * 60 * 1000L
            if (Date().time - mInstallDate.time >= thresholdMillis) {
                clearSharedPreferences(context)
                return true
            } else {
                return false
            }
        }
    }

    fun clearSharedPreferences(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            remove(KEY_INSTALL_DATE)
            remove(KEY_LAUNCH_TIMES)
        }
    }

    /**
     * Set opt out flag. If it is true, the rate dialog will never shown unless app data is cleared.
     */
    @JvmStatic
    fun setOptOut(context: Context, optOut: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            putBoolean(KEY_OPT_OUT, optOut)
        }
    }

    fun wasRated(context: Context): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_OPT_OUT, false)
    }
}
