package com.walhalla.ui.observer;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;

import androidx.lifecycle.LifecycleOwner;

import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;

import com.androidsx.rateme.OnRatingListener;
import com.androidsx.rateme.RateMeDialog;


import com.androidsx.rateme.RateMeDialogTimer;


import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.walhalla.ui.SharedPref;
import com.walhalla.ui.UConst;
import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;

import com.walhalla.ui.R;


public class RateAppModule implements SimpleModule, DefaultLifecycleObserver {

    // alexvarboffin@gmail.com
    public static int[] v0 = new int[]{109, 111, 99, 46, 108, 105, 97, 109, 103, 64, 110, 105, 102, 102, 111, 98, 114, 97, 118, 120, 101, 108, 97};
    private final String rateAppFeedbackEmail;

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String _DIALOG_TAG = "plain-dialog";
    //public static final int REQUEST_CODE_MARKET = 2322;

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (compatActivity != null) {

                DialogFragment builder = new RateMeDialog.Builder(
                        compatActivity.getPackageName(), compatActivity.getString(R.string.app_name))
                        .enableFeedbackByEmail(RateAppModule.this.rateAppFeedbackEmail)
                        //.showAppIcon(R.mipmap.ic_launcher)
                        .setOnRatingListener(new DefaultOnRatingListener() {
                            @Override
                            public void onRating(RatingAction action, float rating) {
                                super.onRating(action, rating);
                                if (OnRatingListener.RatingAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY == action) {
                                    rateApp00();
                                }
                            }
                        })
                        .build();
                builder.show(compatActivity.getSupportFragmentManager(), _DIALOG_TAG);
            }
        }
    };

    private static final String KEY_RATE_TIMEOUT0 = "rate_rate_timeout";

    private static final double DAYS_UNTIL_PROMPT = 1;

    private static final Long ONE_MINUTE = 60 * 1000L;

    private static final Long LEVEL_1_ = ONE_MINUTE * 60;
    private static final Long LEVEL_2_ = ONE_MINUTE * 120;
    private static final Long LEVEL_3_ = ONE_MINUTE * 180;

    private final AppCompatActivity compatActivity;
    private final SharedPref var1;


    //private final ActivityResultLauncher<Intent> default_rate_app_launcher;

    private int launch_count;
    private final int LAUNCHES_UNTIL_PROMPT_ = 5;


    private boolean isRun;

    private boolean new_rate_module = true;
    private ReviewManager reviewManager;

    //(LEVEL_1/*DAYS_UNTIL_PROMPT * 24 * 60 */)
    public RateAppModule(AppCompatActivity context) {
        compatActivity = context;
        this.var1 = SharedPref.getInstance(context);
        this.launch_count = this.var1.appResumeCount();
        //this.rateAppFeedbackEmail=compatActivity.getString(R.string.publisher_feedback_email);
        //this.rateAppFeedbackEmail=compatActivity.getString(R.string.publisher_feedback_email);
        this.rateAppFeedbackEmail = dec0();
        this.reviewManager = ReviewManagerFactory.create(context);// Инициализируем ReviewManager

//        default_rate_app_launcher = compatActivity.registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    final int code = result.getResultCode();
//                    if (code == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        defaultMarketRateCallback(data, code);
//                    } else if (code == Activity.RESULT_CANCELED) {
//                        Intent data = result.getData();
//                        defaultMarketRateCallback(data, code);
//                    } else {
//                        Intent data = result.getData();
//                        defaultMarketRateCallback(data, code);
//                    }
//                });
    }

    private static Long rateLevelTimeout(SharedPref var1, SharedPreferences var0) {
        long RATE_TIMEOUT = var0.getLong(KEY_RATE_TIMEOUT0, 0);
        if (RATE_TIMEOUT == 0) {
            return LEVEL_1_;
        } else if (RATE_TIMEOUT == LEVEL_1_) {
            return LEVEL_2_;
        } else if (RATE_TIMEOUT == LEVEL_2_) {
            return LEVEL_3_;
        } else {
            var1.appRated(true);
            return LEVEL_3_;
        }
    }

//    private void defaultMarketRateCallback(Intent data, int code) {
//        showReviewDialog(compatActivity, data, code);
//        //RateMeDialogTimer.setOptOut(compatActivity, true);
//    }

//    private void showReviewDialog(Context context, Intent data, int code) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Задание выполнено!" + code)
//                .setMessage("Вы успешно оставили отзыв о приложении." + data)
//                .setPositiveButton("OK", (dialog, which) -> {
////                    reviewCompleted = true;
////                    sharedPreferences.edit().putBoolean("review_completed", true).apply();
////                    updateQuestStatus();
//                })
//                .show();
//    }

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

        if ((!var1.appRated()) && (launch_count >= LAUNCHES_UNTIL_PROMPT_)) {
            DLog.d("@@@" + var1.appRated() + " " + launch_count + " " + LAUNCHES_UNTIL_PROMPT_);
            // Get date of first launch
            long date_firstLaunch = var1.date_firstLaunch();

            //Set delay level


            if (date_firstLaunch == 0) {
                date_firstLaunch = System.currentTimeMillis();
                var1.date_firstLaunch(date_firstLaunch);
            }
            SharedPreferences var0 = PreferenceManager.getDefaultSharedPreferences(compatActivity);
            Long delay = rateLevelTimeout(var1, var0);
            //@@

            if (validate(date_firstLaunch, delay)) {
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
        launch_count = LAUNCHES_UNTIL_PROMPT_;
        //appRated(false);
        makeOnResume();
    }

//    public void testLaunch() {
////        AlertDialog dialog = new RateMeDialog(activity, this).create();
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        dialog.setContentView(R.layout.dialog_rate);
////        dialog.show();
//
//        UiRatingDialog dialog = new UiRatingDialog(compatActivity);
//        dialog.setRatingDialogListener(new UiRatingDialog.RatingDialogInterFace() {
//            @Override
//            public void onDismiss() {
//                DLog.d("onDismiss ");
//                appRated(compatActivity, true);
//            }
//
//            @Override
//            public void onSubmit(float rating) {
//                DLog.d("onSubmit: " + rating);
//                appRated(compatActivity, true);
//
//                /*
//                 *
//                 *       http://www.amazon.com/gp/mas/dl/android?p=%1$s
//                 *       market://details?id=%1$s
//                 * */
//
//
////                if (new_rate_module) {
////                    ReviewManager manager = ReviewManagerFactory.create(compatActivity);
////                    Task<ReviewInfo> request = manager.requestReviewFlow();
////                    request.addOnCompleteListener(task -> {
////                        if (task.isSuccessful()) {
////                            // We can get the ReviewInfo object
////                            ReviewInfo reviewInfo = task.getResult();
////                            Task<Void> flow = manager.launchReviewFlow(compatActivity, reviewInfo);
////                            flow.addOnCompleteListener(task0 -> {
////                                if(task0.isSuccessful()){
////                                    DLog.d("@@@");
////                                }
////                            });
////                        } else {
////                            // There was some problem, continue regardless of the result.
////                        }
////                    });
////
////                } else {
//                Launcher.rateUs(compatActivity);
////                }
//
//
//            }
//
//            @Override
//            public void onRatingChanged(float rating) {
//                //DLog.d("onRatingChanged " + rating);
//            }
//        });
//        dialog.showDialog();
//    }


    public static void appRated(Context context, boolean setOrReset /*true if rated*/) {
        SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPref var0 = SharedPref.getInstance(context);
        var0.appRated(setOrReset);
        if (!setOrReset) {
            //New time
            var0.date_firstLaunch(System.currentTimeMillis());
            var1
                    .edit()
                    .putLong(KEY_RATE_TIMEOUT0, rateLevelTimeout(var0, var1))
                    .apply();
        }
    }


    public void appReloadedHandler() {
        if (launch_count < LAUNCHES_UNTIL_PROMPT_ + 1) { // Save reloads num
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


    private void openRateDefault0() {
        try {
            String packageName = compatActivity.getPackageName();
            //String packageName = "com.walhalla.whatismyipaddress";

            Uri uri = Uri.parse(UConst.MARKET_CONSTANT + packageName);
            //Uri uri = Uri.parse(GOOGLE_PLAY_CONSTANT + packageName);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            compatActivity.getApplicationContext().startActivity(intent);
            RateMeDialogTimer.setOptOut(compatActivity, true);

            //default_rate_app_launcher.launch(intent);
            //compatActivity.startActivityForResult(intent, REQUEST_CODE_MARKET);
        } catch (android.content.ActivityNotFoundException ignored) {
        }
    }

    private void rateApp00() {
        try {
            requestAppReview();
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    private void requestAppReview() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Получаем ReviewInfo
                ReviewInfo reviewInfo = task.getResult();
                if (reviewInfo.toString().contains("isNoOp=true")) {
                    openRateDefault0();
                } else {
                    //isNoOp=false
                    // Запускаем диалоговое окно оценки
                    Task<Void> flow = reviewManager.launchReviewFlow(compatActivity, reviewInfo);
                    flow.addOnCompleteListener(reviewFlowTask -> {
                        // Обработка завершения процесса оценки
                        if (reviewFlowTask.isSuccessful()) {
                            //null = reviewFlowTask.getResult()
                            DLog.d("Оценка была успешно поставлена: " + reviewFlowTask.getResult());
                            RateMeDialogTimer.setOptOut(compatActivity, true);
                        } else {
                            // Оценка не была поставлена
                            //Toast.makeText(compatActivity, "Не удалось поставить оценку", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                // Обработка ошибки при запросе оценки
                //Exception exception = task.getException();
                openRateDefault0();
            }
        });
//        request.addOnSuccessListener(new OnSuccessListener<ReviewInfo>() {
//            @Override
//            public void onSuccess(ReviewInfo reviewInfo) {
//                DLog.d("<success>" + reviewInfo);
//            }
//        });
//        request.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                DLog.d("<failure>" + e.getLocalizedMessage());
//            }
//        });
    }

    public String dec0(int[] intArray) {
        char[] strArray = new char[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            strArray[i] = (char) intArray[i];
        }
        return new StringBuilder((String.valueOf(strArray))).reverse().toString();
    }

    public String dec0() {
        return dec0(v0);
    }
}
