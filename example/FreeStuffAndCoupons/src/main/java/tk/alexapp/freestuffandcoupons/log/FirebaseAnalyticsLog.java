package tk.alexapp.freestuffandcoupons.log;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsLog {

    public static void log(Context context, String systemLanguage) {
        // В методе onReceive вашего BroadcastReceiver
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Language Change");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Language Changed");
        bundle.putString("system_language", systemLanguage);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
