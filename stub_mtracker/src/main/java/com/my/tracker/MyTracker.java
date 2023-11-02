package com.my.tracker;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

public class MyTracker {
    public static void onActivityResult(int resultCode, Intent data) {
    }

    public static com.my.tracker.MyTrackerConfig getTrackerConfig() {
        return new MyTrackerConfig();
    }

    public static void trackRegistrationEvent(String custom_user_id, String vk_connect_id) {
    }

    public static void trackInviteEvent() {
    }

    public static void trackEvent(String click_offer, Map<String, String> params) {
    }

    public static void trackLoginEvent(String custom_user_id, String vk_connect_id, Map<String, String> eventCustomParams) {
    }

    public static String handleDeeplink(Intent intent) {
        return "";
    }

    public static void trackLevelEvent() {
    }

    public static MyTrackerParams getTrackerParams() {
        return null;
    }

    public static void initTracker(String mytrackerKey, Context myApp) {
    }
}
