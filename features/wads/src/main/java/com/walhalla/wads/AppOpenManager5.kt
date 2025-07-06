package com.walhalla.wads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver

import androidx.lifecycle.LifecycleOwner

import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.walhalla.library.BuildConfig
import com.walhalla.wads.DLog.d
import com.walhalla.wads.preference.SharedPref
import java.util.Date

//if lang, if rate
/**
 * Не нужен SplashScreen
 */
class AppOpenManager5(private val myApplication: Application, private val AD_UNIT_ID: String) :
    Application.ActivityLifecycleCallbacks {
    private val sessionManager: SharedPref
    private val count: Int

    private var loadTime: Long = 0
    private var currentActivity0: Activity? = null
    private val BuildConfigDEBUG = BuildConfig.DEBUG


    //    public void showAdIfAvailable(@Nullable final Activity activity) {
    //
    //    }
    /**
     * Shows the ad if one isn't already showing.
     */
    fun showAdIfAvailable(activity: Activity?) {
        if (activity != null) {
            d("<RAw>" + "" + activity.javaClass)
        } else {
            d("<RAw" + "")
        }
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable) {
            d("Will show ad.")

            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    //Banner backpressed <--
                    //Or Click To App
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        d("<RA>" + "")
                        this@AppOpenManager5.appOpenAd = null
                        isShowingAd = false
                        fetchAd()

                        // Помечаем, что баннер был показан
                        sessionManager.setBannerShown()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        d("<RA>" + adError.message)
                    }

                    override fun onAdShowedFullScreenContent() {
                        d("<RA>" + "")
                        isShowingAd = true
                    }
                }

            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            if (activity != null) {
                appOpenAd!!.show(activity)
            }
        } else {
            d("Can not show ad.")
            fetchAd()
        }
    }

    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null


    /**
     * Constructor
     */
    constructor(myApplication: Application, appOpen: Int) : this(
        myApplication,
        myApplication.getString(appOpen)
    )

    init {
        sessionManager = SharedPref.getInstance(myApplication)
        myApplication.registerActivityLifecycleCallbacks(this) //@@@ Application.ActivityLifecycleCallbacks
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    // Show the ad (if available) when the app moves to foreground.
                    //if (ACTIVITY_MOVES_TO_FOREGROUND_HANDLE) {
                    if (needRequestAppBanner()) {
                        this@AppOpenManager5.showAdIfAvailable(currentActivity0)
                    }
                    //}
                    //1.
                    d("onStart::::::::")
                }

                override fun onCreate(owner: LifecycleOwner) {
                    //0. DLog.d("onCreate");
                }

                override fun onPause(owner: LifecycleOwner) {
                    if (BuildConfigDEBUG) {
                        d("###" + "<pause>")
                    }
                    sessionManager.resetBannerShown()
                }
            })

        count = sessionManager.launchCount
    }

    private fun needRequestAppBanner(): Boolean {
        if (count <= 0) {
            sessionManager.launchCount = 1
            return false
        }
        // Проверяем, был ли уже показан баннер в текущей сессии
        return (!sessionManager.isBannerAlreadyShown)
    }


    /**
     * LifecycleObserver methods
     */
    /**
     * Request an ad
     */
    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable) {
            return
        }

        loadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                override fun onAdLoaded(ad: AppOpenAd) {
                    this@AppOpenManager5.appOpenAd = ad
                    this@AppOpenManager5.loadTime = (Date()).time
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error.
                 */
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                }
            }
        val request = adRequest
        loadCallback?.let {
            AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, it
            )
        }
    }

    private val adRequest: AdRequest
        /**
         * Creates and returns ad request.
         */
        get() = AdRequest.Builder().build()

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     * Чтобы убедиться, что вы не показываете рекламу с истекшим сроком действия,
     * добавьте в AppOpenManager метод, который проверяет, сколько времени прошло с момента
     * загрузки вашей ссылки на рекламу. Затем используйте этот метод, чтобы проверить,
     * действует ли объявление. Обновите свой метод AppOpenManager следующим образом:
     */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = (Date()).time - this.loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return (dateDifference < (numMilliSecondsPerHour * numHours))
    }

    val isAdAvailable: Boolean
        /**
         * Utility method that checks if ad exists and can be shown.
         */
        get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is AdActivity) {
            val tmp = appOpenAd != null
            currentActivity0 = null
            if (tmp) {
                appOpenAd = null
            }
            d("###" + activity.getLocalClassName() + " " + tmp)
        } else {
            currentActivity0 = activity
            if (BuildConfigDEBUG) {
                //java.lang.Exception: Toast callstack! strTip=###activity.MainActivity
                //Toast.makeText(myApplication, "###" + activity.getLocalClassName(), Toast.LENGTH_SHORT).show();
                d("###" + activity.localClassName)
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is AdActivity) {
            val tmp = appOpenAd != null
            currentActivity0 = null
            if (tmp) {
                appOpenAd = null
            }
            d("###" + activity.getLocalClassName() + " " + tmp)
        } else {
            currentActivity0 = activity
            if (BuildConfigDEBUG) {
                //java.lang.Exception: Toast callstack! strTip=
                // @@@Toast.makeText(myApplication, "@@@" + activity.getLocalClassName(), Toast.LENGTH_SHORT).show();
                d("@@@" + activity.localClassName)
            }
        }
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity0 = null
    }

    companion object {
        private var isShowingAd = false
    }
}