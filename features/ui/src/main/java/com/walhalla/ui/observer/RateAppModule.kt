package com.walhalla.ui.observer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceManager
import com.androidsx.rateme.OnRatingListener.RatingAction
import com.androidsx.rateme.RateMeDialog
import com.androidsx.rateme.RateMeDialogTimer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.walhalla.ui.BuildConfig
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.handleException
import com.walhalla.ui.R
import com.walhalla.ui.SharedPref
import com.walhalla.ui.SharedPref.Companion.getInstance
import com.walhalla.ui.UConst

class RateAppModule(context: AppCompatActivity) : SimpleModule, DefaultLifecycleObserver {
    private val rateAppFeedbackEmail: String

    //public static final int REQUEST_CODE_MARKET = 2322;
    private val handler = Handler()
    private val runnable: Runnable = Runnable {
        if (compatActivity != null) {
            val builder: DialogFragment = RateMeDialog.Builder(
                compatActivity.packageName, compatActivity.getString(R.string.app_name)
            )
                .enableFeedbackByEmail(this@RateAppModule.rateAppFeedbackEmail) //.showAppIcon(R.mipmap.ic_launcher)
                .setOnRatingListener(object : DefaultOnRatingListener() {
                    override fun onRating(action: RatingAction?, rating: Float) {
                        super.onRating(action, rating)
                        if (RatingAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY == action) {
                            rateApp00()
                        }
                    }
                })
                .build()
            builder.show(compatActivity.supportFragmentManager, _DIALOG_TAG)
        }
    }

    private val compatActivity: AppCompatActivity?
    private val var1: SharedPref


    //private final ActivityResultLauncher<Intent> default_rate_app_launcher;
    private var launch_count: Int
    private val LAUNCHES_UNTIL_PROMPT_ = 5


    private var isRun = false

    private val new_rate_module = true
    private val reviewManager: ReviewManager

    //(LEVEL_1/*DAYS_UNTIL_PROMPT * 24 * 60 */)
    init {
        compatActivity = context
        this.var1 = getInstance(context)
        this.launch_count = this.var1.appResumeCount()
        //this.rateAppFeedbackEmail=compatActivity.getString(R.string.publisher_feedback_email);
        //this.rateAppFeedbackEmail=compatActivity.getString(R.string.publisher_feedback_email);
        this.rateAppFeedbackEmail = dec0()
        this.reviewManager = ReviewManagerFactory.create(context) // Инициализируем ReviewManager

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
    override fun onStop(owner: LifecycleOwner) {
        //if (dialog != null) { dialog.dismiss(); dialog = null; }

        if (compatActivity != null) {
            val prev = compatActivity.getSupportFragmentManager().findFragmentByTag(_DIALOG_TAG)
            if (prev != null) {
                val df = prev as DialogFragment
                df.dismiss()
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        handler.removeCallbacks(runnable)
    }

    override fun onResume(owner: LifecycleOwner) {
        makeOnResume()
    }

    private fun makeOnResume() {
        if ((!var1.appRated()) && (launch_count >= LAUNCHES_UNTIL_PROMPT_)) {
            d("@@@" + var1.appRated() + " " + launch_count + " " + LAUNCHES_UNTIL_PROMPT_)
            // Get date of first launch
            var date_firstLaunch = var1.date_firstLaunch()


            //Set delay level
            if (date_firstLaunch == 0L) {
                date_firstLaunch = System.currentTimeMillis()
                var1.date_firstLaunch(date_firstLaunch)
            }
            val var0 = PreferenceManager.getDefaultSharedPreferences(compatActivity!!)
            val delay: Long = rateLevelTimeout(var1, var0)

            //@@
            if (validate(date_firstLaunch, delay)) {
//                AlertDialog dialog = new RateMeDialog(activity, this).create();
//                dialog.show();
                if (!isRun) {
                    handler.postDelayed(runnable, 2000)
                    isRun = true
                }
            }
        }

        //testLaunch();
    }

    private fun validate(date_firstLaunch: Long, delay: Long): Boolean {
//        if (DEBUG) {
//            long millis = date_firstLaunch + delay - System.currentTimeMillis();
//            DLog.d(millis + "ms");
//            DLog.d(String.format("%d min, %d sec",
//                    TimeUnit.MILLISECONDS.toMinutes(millis),
//                    TimeUnit.MILLISECONDS.toSeconds(millis) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
//            ));
//        }
        return System.currentTimeMillis() >= date_firstLaunch + delay
    }

    fun launchIfNotRated() {
        launch_count = LAUNCHES_UNTIL_PROMPT_
        //appRated(false);
        makeOnResume()
    }

    fun appReloadedHandler() {
        if (launch_count < LAUNCHES_UNTIL_PROMPT_ + 1) { // Save reloads num
            ++launch_count
            var1.appResumeCount(launch_count)
        }
    }

    //reset all
    fun launchNow() {
        var1.appRated(false)
        var1.date_firstLaunch(-999)
        launch_count = 99999
        makeOnResume()
    }


    private fun openRateDefault0() {
        try {
            val packageName = compatActivity!!.getPackageName()

            //String packageName = "com.walhalla.whatismyipaddress";
            val uri = Uri.parse(UConst.MARKET_CONSTANT + packageName)

            //Uri uri = Uri.parse(GOOGLE_PLAY_CONSTANT + packageName);
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            compatActivity.getApplicationContext().startActivity(intent)
            RateMeDialogTimer.setOptOut(compatActivity, true)

            //default_rate_app_launcher.launch(intent);
            //compatActivity.startActivityForResult(intent, REQUEST_CODE_MARKET);
        } catch (ignored: ActivityNotFoundException) {
        }
    }

    private fun rateApp00() {
        try {
            requestAppReview()
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun requestAppReview() {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener(OnCompleteListener { task: Task<ReviewInfo>? ->
            if (task!!.isSuccessful()) {
                // Получаем ReviewInfo
                val reviewInfo = task.getResult()
                if (reviewInfo.toString().contains("isNoOp=true")) {
                    openRateDefault0()
                } else {
                    //isNoOp=false
                    // Запускаем диалоговое окно оценки
                    val flow = reviewManager.launchReviewFlow(compatActivity!!, reviewInfo)
                    flow.addOnCompleteListener(OnCompleteListener { reviewFlowTask: Task<Void?>? ->
                        // Обработка завершения процесса оценки
                        if (reviewFlowTask!!.isSuccessful()) {
                            //null = reviewFlowTask.getResult()
                            d("Оценка была успешно поставлена: " + reviewFlowTask.getResult())
                            RateMeDialogTimer.setOptOut(compatActivity, true)
                        } else {
                            // Оценка не была поставлена
                            //Toast.makeText(compatActivity, "Не удалось поставить оценку", Toast.LENGTH_SHORT).show();
                        }
                    })
                }
            } else {
                // Обработка ошибки при запросе оценки
                //Exception exception = task.getException();
                openRateDefault0()
            }
        })
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

    @JvmOverloads
    fun dec0(intArray: IntArray = v0): String {
        val strArray = CharArray(intArray.size)
        for (i in intArray.indices) {
            strArray[i] = intArray[i].toChar()
        }
        return StringBuilder((String(strArray))).reverse().toString()
    }

    companion object {
        // alexvarboffin@gmail.com
        var v0: IntArray = intArrayOf(
            109,
            111,
            99,
            46,
            108,
            105,
            97,
            109,
            103,
            64,
            110,
            105,
            102,
            102,
            111,
            98,
            114,
            97,
            118,
            120,
            101,
            108,
            97
        )
        private val DEBUG = BuildConfig.DEBUG
        private const val _DIALOG_TAG = "plain-dialog"

        private const val KEY_RATE_TIMEOUT0 = "rate_rate_timeout"

        private const val DAYS_UNTIL_PROMPT = 1.0

        private val ONE_MINUTE = 60 * 1000L

        private val LEVEL_1_: Long = ONE_MINUTE * 60
        private val LEVEL_2_: Long = ONE_MINUTE * 120
        private val LEVEL_3_: Long = ONE_MINUTE * 180

        private fun rateLevelTimeout(var1: SharedPref, var0: SharedPreferences): Long {
            val RATE_TIMEOUT = var0.getLong(KEY_RATE_TIMEOUT0, 0)
            if (RATE_TIMEOUT == 0L) {
                return LEVEL_1_
            } else if (RATE_TIMEOUT == LEVEL_1_) {
                return LEVEL_2_
            } else if (RATE_TIMEOUT == LEVEL_2_) {
                return LEVEL_3_
            } else {
                var1.appRated(true)
                return LEVEL_3_
            }
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
        fun appRated(context: Context, setOrReset: Boolean /*true if rated*/) {
            val var1 = PreferenceManager.getDefaultSharedPreferences(context)
            val var0 = getInstance(context)
            var0.appRated(setOrReset)
            if (!setOrReset) {
                //New time
                var0.date_firstLaunch(System.currentTimeMillis())
                var1
                    .edit()
                    .putLong(KEY_RATE_TIMEOUT0, rateLevelTimeout(var0, var1))
                    .apply()
            }
        }
    }
}
