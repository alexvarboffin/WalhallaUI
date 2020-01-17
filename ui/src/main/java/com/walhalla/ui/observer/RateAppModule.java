package com.walhalla.ui.observer;

import android.app.AlertDialog;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

import android.content.Context;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;


import com.ratingdialog.simple.UiRatingDialog;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.Module_U;

import com.walhalla.ui.R;

import java.util.concurrent.TimeUnit;


public class RateAppModule implements SimpleModule,
        LifecycleObserver {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String KEY_RATED = "key_not_show_again";
    private static final String KEY_RELOADED = "key_launch_count";
    private static final String KEY_RATE_TIMEOUT = "key_rate_timeout";

    private static final double DAYS_UNTIL_PROMPT = 1;
    private static final String TAG = "@@@";

    private static final Long ONE_MINUTE = 60 * 1000L;

    private static final Long LEVEL_1 = ONE_MINUTE * 15;
    private static final Long LEVEL_2 = ONE_MINUTE * 20;
    private static final Long LEVEL_3 = ONE_MINUTE * 25;

    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    private int launch_count;
    private int LAUNCHES_UNTIL_PROMPT = 0;

    private static final String DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH";

    //(LEVEL_1/*DAYS_UNTIL_PROMPT * 24 * 60 */)
    public RateAppModule(Context context) {
        this.mContext = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.launch_count = appReloadedCount();
    }


//    public RateAppModule(Context context, int lap,) {
//        LAUNCHES_UNTIL_PROMPT = lap
//        this.mContext = context;
//        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        launch_count = appReloadedCount();
//    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {

        if ((!appRated()) && (launch_count >= LAUNCHES_UNTIL_PROMPT)) {

            if (DEBUG) {
                Log.d(TAG, "Rated? - " + appRated()
                        + " " + launch_count + "/" + LAUNCHES_UNTIL_PROMPT
                        + " " + mSharedPreferences.getLong(DATE_FIRST_LAUNCH, 0));
            }

            // Get date of first launch
            long date_firstLaunch = mSharedPreferences.getLong(DATE_FIRST_LAUNCH, 0);

            //Set delay level


            if (date_firstLaunch == 0) {
                date_firstLaunch = System.currentTimeMillis();
                mSharedPreferences.edit().putLong(DATE_FIRST_LAUNCH, date_firstLaunch).apply();
            }

            if (validate(date_firstLaunch, rateLevelTimeout())) {
                AlertDialog dialog = new RatingPopUp(mContext, this).create();
                dialog.show();
            }
        }
    }

    private Long rateLevelTimeout() {
        long RATE_TIMEOUT = mSharedPreferences.getLong(KEY_RATE_TIMEOUT, 0);
        if (RATE_TIMEOUT == 0) {
            return LEVEL_1;
        } else if (RATE_TIMEOUT == LEVEL_1) {
            return LEVEL_2;
        } else if (RATE_TIMEOUT == LEVEL_2) {
            return LEVEL_3;
        } else {
            mSharedPreferences.edit().putBoolean(KEY_RATED, true).apply();
            return LEVEL_3;
        }
    }

    private boolean validate(Long date_firstLaunch, Long delay) {
        if (DEBUG) {
            long millis = date_firstLaunch + delay - System.currentTimeMillis();
            Log.i(TAG, millis + "ms");
            Log.i(TAG, String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            ));
        }
        return System.currentTimeMillis() >= date_firstLaunch + delay;
    }

    public void launchIfNotRated() {
        launch_count = LAUNCHES_UNTIL_PROMPT;
        //appRated(false);
        onResume();
    }

    public void testLaunch() {
//        AlertDialog dialog = new RatingPopUp(mContext, this).create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_rate);
//        dialog.show();

        UiRatingDialog dialog = new UiRatingDialog(mContext);
        dialog.setRatingDialogListener(new UiRatingDialog.RatingDialogInterFace() {
            @Override
            public void onDismiss() {
                Log.v(TAG, "onDismiss ");
                appRated(true);
            }

            @Override
            public void onSubmit(float rating) {
                Log.d(TAG, "onSubmit: " + rating);
                appRated(true);

                /*
                 *
                 *       http://www.amazon.com/gp/mas/dl/android?p=%1$s
                 *       market://details?id=%1$s
                 * */


                Module_U.rateUs(mContext);
            }

            @Override
            public void onRatingChanged(float rating) {
                Log.v(TAG, "onRatingChanged " + rating);
            }
        });
        dialog.showDialog();
    }

    /**
     * Getter and setter
     *
     * @param appReloaded
     */
    private void appReloadedCount(int appReloaded) {
        mSharedPreferences.edit().putInt(KEY_RELOADED, appReloaded).apply();
    }

    private int appReloadedCount() {
        return mSharedPreferences.getInt(KEY_RELOADED, 0);
    }


    public void appRated(boolean setOrReset) {
        mSharedPreferences.edit().putBoolean(KEY_RATED, setOrReset).apply();
        if (!setOrReset) {
            //New time
            mSharedPreferences
                    .edit()
                    .putLong(DATE_FIRST_LAUNCH, System.currentTimeMillis())
                    .apply();
            mSharedPreferences
                    .edit()
                    .putLong(KEY_RATE_TIMEOUT, rateLevelTimeout())
                    .apply();
        }
    }

    private boolean appRated() {
        return mSharedPreferences.getBoolean(KEY_RATED, false);
    }

    public void appReloadedHandler() {
        if (launch_count < LAUNCHES_UNTIL_PROMPT + 1) { // Save reloads num
            ++launch_count;
            appReloadedCount(launch_count);
        }
    }


    private static class RatingPopUp extends AlertDialog.Builder {


        RatingPopUp(final Context context, RateAppModule rateAppModule) {
            super(context);


            this.setTitle(R.string.rate_title);
            Resources res = context.getResources();
            String message = String.format(
                    res.getString(R.string.rate_text), res.getString(R.string.app_name));

            // Set dialog share_message
            setMessage(message);
            setCancelable(false);
            setPositiveButton(context.getString(android.R.string.yes), (dialog, id) -> {

                rateAppModule.appRated(true);

                /*
                 *
                 *       http://www.amazon.com/gp/mas/dl/android?p=%1$s
                 *       market://details?id=%1$s
                 * */


                Module_U.rateUs(context);
                dialog.cancel();
            }).setNegativeButton(context.getString(android.R.string.cancel), (dialog, id) -> {
                rateAppModule.appRated(false);
                dialog.cancel();
            });

        }
    }
}
