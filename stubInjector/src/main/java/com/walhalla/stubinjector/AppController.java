package com.walhalla.stubinjector;

import android.app.Application;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.my.tracker.MyTracker;
import com.my.tracker.MyTrackerConfig;
import com.my.tracker.MyTrackerParams;
import com.onesignal.OneSignal;
import com.walhalla.ui.DLog;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import org.apache.cordova.base.AppConfig;

import java.util.Map;

public class AppController {
    public static final String KEY_DEEP_LINK = "key_deep_link";

    public void init(Application application, AppConfig c0) {

        if (c0 == null) {
            return;
        }

        if (!TextUtils.isEmpty(c0.APP_METRICA_KEY)) {
            ////////////////////////////////////////////////////////////////////////////////////
            // Creating an extended library configuration.
            YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(c0.APP_METRICA_KEY).build();
            // Initializing the AppMetrica SDK.
            YandexMetrica.activate(application.getApplicationContext(), config);
            // Automatic tracking of user activity.
            YandexMetrica.enableActivityAutoTracking(application);
            ////////////////////////////////////////////////////////////////////////////////////
        }

        if (c0.FACEBOOK_ENABLED) {

//            try {
//                FacebookSdk.sdkInitialize(getApplicationContext());
//                AppEventsLogger.activateApp(this);
//                printHashKey(this);
//            } catch (Exception e) {
//                DLog.handleException(e);
//            }

//            try {
////            FirebaseOptions options = new FirebaseOptions.Builder()
////                    .setApplicationId("1087151334766") //APP ID Required for Analytics.
////                    .setProjectId("1087151334766") //PROJECT ID Required for Firebase Installations.
////                    .setApiKey(getString(R.string.api_key)) // Required for Auth.
////                    .build();
////            com.google.firebase.FirebaseApp.initializeApp(this, options, getString(R.string.app_name));
//                com.google.firebase.FirebaseApp.initializeApp(this);
//            } catch (Exception e) {
//                DLog.handleException(e);
//            }
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
            String deepLink = preferences.getString(KEY_DEEP_LINK, null);

            ////////////////////////////////////////////////////////////////////////////////////
            FacebookSdk.sdkInitialize(application.getApplicationContext());
            FacebookSdk.setAutoLogAppEventsEnabled(true);
            AppEventsLogger logger = AppEventsLogger.newLogger(application);
            FacebookSdk.setAdvertiserIDCollectionEnabled(true);
            FacebookSdk.setAutoInitEnabled(true);
            FacebookSdk.fullyInitialize();
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

            AppEventsLogger.activateApp(application);

            AppLinkData.fetchDeferredAppLinkData(application,
                    appLinkData -> {
                        if (DLog.nonNull(appLinkData)) {
                            try {
                                if (null != appLinkData) {
                                    DLog.d("$$$ " + appLinkData.toString()); //$$$ com.facebook.applinks.AppLinkData@2de8d30
                                    DLog.d("$$$ " + appLinkData.getPromotionCode()); //null
                                    DLog.d("$$$ " + appLinkData.getAppLinkData().toString());//{}
                                    DLog.d("$$$ " + appLinkData.getTargetUri()); //app://kingslo?000=000

                                    if (appLinkData.getRef() != null) {//Может быть NULL
                                        DLog.d("$$$ " + appLinkData.getRef());
                                    }
                                    if (appLinkData.getRefererData() != null) {
                                        DLog.d("$$$ " + appLinkData.getRefererData().toString());
                                    }

                                    //Получен deeplink
                                    //app://kingslo?000=000

                                    if (appLinkData.getTargetUri() != null) {
                                        DLog.d("" + appLinkData.getTargetUri().toString());
                                        final String tmp = appLinkData.getTargetUri().toString();
                                        String[] aa = tmp.split("target_url=");
                                        String rawUrl = tmp;
                                        if (aa.length > 1) {
                                            rawUrl = aa[1];
                                        }
                                        preferences.edit().putString(KEY_DEEP_LINK, rawUrl).apply();
                                        //PEREHOD_S_DEEPLINKOM();
                                    }
                                }
                            } catch (Exception e) {
                                DLog.handleException(e);
                            }
                        }
                    }
            );

            FacebookSdk.setAutoInitEnabled(true);
            FacebookSdk.fullyInitialize();
            ////////////////////////////////////////////////////////////////////////////////////
        }

        if (!TextUtils.isEmpty(c0.APPSFLYER_DEV_KEY)) {
            ////////////////////////////////////////////////////////////////////////////////////
            AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
                @Override
                public void onConversionDataSuccess(Map<String, Object> conversionData) {

                    for (String attrName : conversionData.keySet()) {
                        DLog.d("attribute: " + attrName + " = " + conversionData.get(attrName));
                    }
                }

                @Override
                public void onConversionDataFail(String errorMessage) {
                    DLog.d("error getting conversion data: " + errorMessage);
                }

                @Override
                public void onAppOpenAttribution(Map<String, String> attributionData) {

                    for (String attrName : attributionData.keySet()) {
                        DLog.d("attribute: " + attrName + " = " + attributionData.get(attrName));
                        //.....
                    }

                }

                @Override
                public void onAttributionFailure(String errorMessage) {
                    DLog.d("error onAttributionFailure : " + errorMessage);
                }
            };
            AppsFlyerLib.getInstance().init(c0.APPSFLYER_DEV_KEY, conversionListener, application);
            //AppsFlyerLib.getInstance().startTracking(this);
            //AppsFlyerLib.getInstance().startTracking(this);
            ////////////////////////////////////////////////////////////////////////////////////
        }

        if (!TextUtils.isEmpty(c0.MYTRACKER_KEY)) {
            // При необходимости, настройте конфигурацию трекера
            MyTrackerParams trackerParams = MyTracker.getTrackerParams();
            MyTrackerConfig trackerConfig = MyTracker.getTrackerConfig();
            // …
            // Настройте параметры трекера
            // …
            // Инициализируйте трекер
            MyTracker.initTracker(c0.MYTRACKER_KEY, application);
        }
        if (!TextUtils.isEmpty(c0.ONESIGNAL_APP_ID)) {
            // OneSignal Initialization
//            OneSignal.startInit(this)
//                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                    //.setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                    .unsubscribeWhenNotificationsAreDisabled(true)
//                    .autoPromptLocation(true)
//                    .init();

            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
            //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);

            // OneSignal Initialization
            OneSignal.initWithContext(application);
            OneSignal.setAppId(c0.ONESIGNAL_APP_ID);
//            DLog.d("OneSignal Initialization");
//            OneSignal.startInit(this)
//                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                    //.setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                    //--.unsubscribeWhenNotificationsAreDisabled(true)
//                    //.autoPromptLocation(true)
//                    .init();

            OneSignal.unsubscribeWhenNotificationsAreDisabled(false);
//            OSDeviceState device = OneSignal.getDeviceState();
//            if (device != null) {
//                String email = device.getEmailAddress();
//                String emailId = device.getEmailUserId();
//                String pushToken = device.getPushToken();
//                String userId = device.getUserId();
//
//                boolean enabled = device.areNotificationsEnabled();
//                boolean subscribed = device.isSubscribed();
//                boolean pushDisabled = device.isPushDisabled();
//
//                DLog.d("[" + enabled + "] " + subscribed + " " + pushDisabled);
//                DLog.d(String.valueOf(device.toJSONObject()));
        }

    }

//    void log() {
//        Map<String, Object> eventValues = new HashMap<>();
//        eventValues.put(AFInAppEventParameterName.REVENUE, 1200);
//        eventValues.put(AFInAppEventParameterName.CURRENCY, "JPY");
//        eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, "Shoes");
//        AppsFlyerLib.getInstance().logEvent(this, AFInAppEventType.PURCHASE, eventValues);
//    }
}
