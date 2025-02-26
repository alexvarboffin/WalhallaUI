package org.apache.cordova.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.walhalla.ui.DLog;


public class InstallListener extends BroadcastReceiver {

    private static final String KEY_REFERRER = "referrer";

    //String webview_url = Resources.getSystem().getString(R.string.app_url);

    @Override
    public void onReceive(Context context, Intent intent) {
        String rawReferrerString = intent.getStringExtra(KEY_REFERRER);
        if (rawReferrerString != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.edit().putString(KEY_REFERRER, rawReferrerString).apply();

            DLog.d("onReceive: " + rawReferrerString);

//            HttpGetRequest getRequest = new HttpGetRequest();
//            try {
//                String device_id = Util.phoneId(MainActivity.getAppContext().getApplicationContext());
//                String tracker = MainActivity.getAppContext().getString(R.string.app_url) + "?"
//                        + rawReferrerString.replace("%26", "&")
//                        .replace("%3D", "=")
//                        + "&id=" + device_id + "&event=first_install_from_google_play";
//                if (BuildConfig.DEBUG) {
//                    Log.i(TAG, "REF_TRACK: " + tracker);
//                }
//                getRequest.execute(tracker).get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}