package org.apache.web;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import com.walhalla.ui.DLog;

import org.apache.utils.CustomTabUtils;

public class CustomTabs {

    private final Context context;
    private final CustomTabsIntent customTabsIntent;


    public CustomTabs(Activity context) {

        this.context = context;
        this.customTabsIntent = CustomTabUtils.customWeb(context);
    }


    public void launchUrl(String url) {
        try {
//        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
//        Bundle bundle = new Bundle();
//        bundle.putString("goz_ad_click", "1");
//        mFirebaseAnalytics.logEvent("in_offer_open_click", bundle);
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }
}
