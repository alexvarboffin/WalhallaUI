package com.walhalla.ui.observer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.androidsx.rateme.RateMeDialog;


import com.ratingdialog.simple.UiRatingDialog;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;

import com.walhalla.ui.R;


public class RateAppModule implements SimpleModule,
        LifecycleObserver {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String _DIALOG_TAG = "plain-dialog";

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (compatActivity != null) {

                DialogFragment builder = new RateMeDialog.Builder(
                        compatActivity.getPackageName(), compatActivity.getString(R.string.app_name))
                        .enableFeedbackByEmail(compatActivity.getString(R.string.publisher_feedback_email))
                        .build();
                builder.show(compatActivity.getSupportFragmentManager(), _DIALOG_TAG);
            }
        }
    };

    private static final String KEY_RATED = "rate_not_show_again_a";
    private static final String KEY_RELOADED = "rate_launch_count_a";
    private static final String KEY_RATE_TIMEOUT = "rate_rate_timeout_a";

    private static final double DAYS_UNTIL_PROMPT = 1;

    private static final Long ONE_MINUTE = 60 * 1000L;

    private static final Long LEVEL_1 = ONE_MINUTE * 35;
    private static final Long LEVEL_2 = ONE_MINUTE * 65;
    private static final Long LEVEL_3 = ONE_MINUTE * 180;

    private final AppCompatActivity compatActivity;
    private final SharedPreferences mSharedPreferences;

    private int launch_count;
    private final int LAUNCHES_UNTIL_PROMPT = 0;

    private static final String DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH_";
    private boolean isRun;

    private boolean new_rate_module = true;


    //(LEVEL_1/*DAYS_UNTIL_PROMPT * 24 * 60 */)
    public RateAppModule(AppCompatActivity context) {
        compatActivity = context;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.launch_count = appReloadedCount();
    }


//    public RateAppModule(Context context, int lap,) {
//        LAUNCHES_UNTIL_PROMPT = lap
//        this.activity = context;
//        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        launch_count = appReloadedCount();
//    }


    //UPDATED 09.09.21

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStop() {

        //if (dialog != null) { dialog.dismiss(); dialog = null; }
        Fragment prev = compatActivity.getSupportFragmentManager().findFragmentByTag(_DIALOG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
            DLog.d("@@@@@@@@@@@@@@");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        handler.removeCallbacks(runnable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {

        if ((!appRated()) && (launch_count >= LAUNCHES_UNTIL_PROMPT)) {

            if (DEBUG) {
                DLog.d("Rated? - " + appRated()
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

            if (validate(date_firstLaunch, rateLevelTimeout(mSharedPreferences))) {
//                AlertDialog dialog = new RateMeDialog(activity, this).create();
//                dialog.show();
                if (!isRun) {
                    handler.postDelayed(runnable, 2000);
                    isRun = true;
                }
            }
        }

        //testLaunch();
    }


    private boolean validate(Long date_firstLaunch, Long delay) {
//        if (DEBUG) {
//            long millis = date_firstLaunch + delay - System.currentTimeMillis();
//            DLog.d(millis + "ms");
//            DLog.d(String.format("%d min, %d sec",
//                    TimeUnit.MILLISECONDS.toMinutes(millis),
//                    TimeUnit.MILLISECONDS.toSeconds(millis) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//            ));
//        }
        return System.currentTimeMillis() >= date_firstLaunch + delay;
    }

    public void launchIfNotRated() {
        launch_count = LAUNCHES_UNTIL_PROMPT;
        //appRated(false);
        onResume();
    }

    public void testLaunch() {
//        AlertDialog dialog = new RateMeDialog(activity, this).create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_rate);
//        dialog.show();

        UiRatingDialog dialog = new UiRatingDialog(compatActivity);
        dialog.setRatingDialogListener(new UiRatingDialog.RatingDialogInterFace() {
            @Override
            public void onDismiss() {
                DLog.d("onDismiss ");
                appRated(compatActivity, true);
            }

            @Override
            public void onSubmit(float rating) {
                DLog.d("onSubmit: " + rating);
                appRated(compatActivity, true);

                /*
                 *
                 *       http://www.amazon.com/gp/mas/dl/android?p=%1$s
                 *       market://details?id=%1$s
                 * */


//                if (new_rate_module) {
//                    ReviewManager manager = ReviewManagerFactory.create(compatActivity);
//                    Task<ReviewInfo> request = manager.requestReviewFlow();
//                    request.addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            // We can get the ReviewInfo object
//                            ReviewInfo reviewInfo = task.getResult();
//                            Task<Void> flow = manager.launchReviewFlow(compatActivity, reviewInfo);
//                            flow.addOnCompleteListener(task0 -> {
//                                if(task0.isSuccessful()){
//                                    DLog.d("@@@@@@@@@@@");
//                                }
//                            });
//                        } else {
//                            // There was some problem, continue regardless of the result.
//                        }
//                    });
//
//                } else {
                Module_U.rateUs(compatActivity);
//                }


            }

            @Override
            public void onRatingChanged(float rating) {
                //DLog.d("onRatingChanged " + rating);
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


    public static void appRated(Context context, boolean setOrReset) {
        SharedPreferences var0 = PreferenceManager.getDefaultSharedPreferences(context);
        var0.edit().putBoolean(KEY_RATED, setOrReset).apply();
        if (!setOrReset) {
            //New time
            var0
                    .edit()
                    .putLong(DATE_FIRST_LAUNCH, System.currentTimeMillis())
                    .apply();
            var0
                    .edit()
                    .putLong(KEY_RATE_TIMEOUT, rateLevelTimeout(var0))
                    .apply();
        }
    }

    private static Long rateLevelTimeout(SharedPreferences var0) {
        long RATE_TIMEOUT = var0.getLong(KEY_RATE_TIMEOUT, 0);
        if (RATE_TIMEOUT == 0) {
            return LEVEL_1;
        } else if (RATE_TIMEOUT == LEVEL_1) {
            return LEVEL_2;
        } else if (RATE_TIMEOUT == LEVEL_2) {
            return LEVEL_3;
        } else {
            var0.edit().putBoolean(KEY_RATED, true).apply();
            return LEVEL_3;
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

    public void launchNow() {
        mSharedPreferences.edit()
                .putBoolean(KEY_RATED, false)
                .putLong(DATE_FIRST_LAUNCH, -999).apply();
        launch_count = 99999;
        onResume();
    }
}
