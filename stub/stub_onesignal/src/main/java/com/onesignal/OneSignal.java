package com.onesignal;

import android.app.Application;

public class OneSignal {
    public static void initWithContext(Application application) {
    }

    public static void setAppId(String onesignal_app_id) {
    }

    public static void setLogLevel(LOG_LEVEL verbose, LOG_LEVEL none) {
    }

    public static void unsubscribeWhenNotificationsAreDisabled(boolean b) {
    }

    public static OSDeviceState getDeviceState() {
        return null;
    }

    public enum LOG_LEVEL {
        VERBOSE, NONE
       
    }
}
