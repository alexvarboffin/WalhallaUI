package com.walhalla.ui.observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.androidsx.rateme.RateMeDialog;


import com.ratingdialog.simple.UiRatingDialog;
import com.walhalla.core.SharedPref;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;
import com.walhalla.ui.Module_U;

import com.walhalla.ui.R;


public class RateAppModule implements SimpleModule,
        DefaultLifecycleObserver {

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


    private static final String KEY_RATE_TIMEOUT = "rate_rate_timeout";

    private static final double DAYS_UNTIL_PROMPT = 1;

    private static final Long ONE_MINUTE = 60 * 1000L;

    private static final Long LEVEL_1 = ONE_MINUTE * 35;
    private static final Long LEVEL_2 = ONE_MINUTE * 65;
    private static final Long LEVEL_3 = ONE_MINUTE * 180;

    private final AppCompatActivity compatActivity;
    private final SharedPref var1;

    private int launch_count;
    private final int LAUNCHES_UNTIL_PROMPT = 0;


    private boolean isRun;

    private boolean new_rate_module = true;


    //(LEVEL_1/*DAYS_UNTIL_PROMPT * 24 * 60 */)
    public RateAppModule(AppCompatActivity context) {
        compatActivity = context;
        this.var1 = new SharedPref(context);
        this.launch_count = this.var1.appResumeCount();
    }


//    public RateAppModule(Context context, int lap,) {
//        LAUNCHES_UNTIL_PROMPT = lap
//        this.activity = context;
//        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        launch_count = appReloadedCount();
//    }


    //UPDATED 06.08.22

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {

        //if (dialog != null) { dialog.dismiss(); dialog = null; }
        if (compatActivity != null) {
            Fragment prev = compatActivity.getSupportFragmentManager().findFragmentByTag(_DIALOG_TAG);
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }
        }
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        makeOnResume();
    }

    private void makeOnResume() {
        DLog.d("@@@");
        if ((!var1.appRated()) && (launch_count >= LAUNCHES_UNTIL_PROMPT)) {

            // Get date of first launch
            long date_firstLaunch = var1.date_firstLaunch();

            //Set delay level


            if (date_firstLaunch == 0) {
                date_firstLaunch = System.currentTimeMillis();
                var1.date_firstLaunch(date_firstLaunch);
            }
            SharedPreferences var0 = PreferenceManager.getDefaultSharedPreferences(compatActivity);
            if (validate(date_firstLaunch, rateLevelTimeout(var1, var0))) {
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
        makeOnResume();
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
//                                    DLog.d("@@@");
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


    public static void appRated(Context context, boolean setOrReset) {
        SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPref var0 = new SharedPref(context);
        var0.appRated(setOrReset);
        if (!setOrReset) {
            //New time
            var0.date_firstLaunch(System.currentTimeMillis());
            var1
                    .edit()
                    .putLong(KEY_RATE_TIMEOUT, rateLevelTimeout(var0, var1))
                    .apply();
        }
    }

    private static Long rateLevelTimeout(SharedPref var1, SharedPreferences var0) {
        long RATE_TIMEOUT = var0.getLong(KEY_RATE_TIMEOUT, 0);
        if (RATE_TIMEOUT == 0) {
            return LEVEL_1;
        } else if (RATE_TIMEOUT == LEVEL_1) {
            return LEVEL_2;
        } else if (RATE_TIMEOUT == LEVEL_2) {
            return LEVEL_3;
        } else {
            var1.appRated(true);
            return LEVEL_3;
        }
    }


    public void appReloadedHandler() {
        if (launch_count < LAUNCHES_UNTIL_PROMPT + 1) { // Save reloads num
            ++launch_count;
            var1.appResumeCount(launch_count);
        }
    }

    //reset all
    public void launchNow() {
        var1.appRated(false);
        var1.date_firstLaunch(-999);
        launch_count = 99999;
        makeOnResume();
    }
}
