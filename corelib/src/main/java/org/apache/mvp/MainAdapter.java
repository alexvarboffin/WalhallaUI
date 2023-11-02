package org.apache.mvp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import org.apache.cordova.BuildConfig;
import org.apache.cordova.Const;

import com.walhalla.ui.DLog;


//install referer

public class MainAdapter {

    private final Context context;
    private final MainView mView;
    private final SharedPreferences preferences;
    private InstallReferrerClient var0;

    public MainAdapter(Context context, SharedPreferences a123, MainView view) {
        this.context = context;
        this.preferences = a123;
        this.mView = view;

        //Autopayload
        String ref = preferences.getString(Const.KEY_REFERRER_, null);
        if (ref == null) {
            installReferrer(context);
        }
    }

    private void installReferrer(Context context) {
        try {
            var0 = InstallReferrerClient.newBuilder(context).build();
            var0.startConnection(installReferrerStateListener);
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    /**
     * Отслеживаем источник установки, referer
     * https://developer.android.com/google/play/installreferrer/library
     * <p>
     * //Facebook App Ads Referral URLs
     * https://developers.facebook.com/docs/app-ads/install-referrer/
     */

    private final InstallReferrerStateListener installReferrerStateListener = new InstallReferrerStateListener() {
        @Override
        public void onInstallReferrerSetupFinished(int responseCode) {
            switch (responseCode) {
                case InstallReferrerClient.InstallReferrerResponse.OK:
                    // Соединение установлено
                    try {
                        ReferrerDetails response = var0.getInstallReferrer();
                        String referrerUrl = response.getInstallReferrer();
                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
                        long appInstallTime = response.getInstallBeginTimestampSeconds();
                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        //utm_source=google-play&utm_medium=organic
                        if (mView != null) {
                            mView.dappend("OK: " + referrerUrl + "@@");
                        }
                        preferences.edit().putString(Const.KEY_REFERRER_, referrerUrl).apply();

                        //Referrer
                        DLog.d("REF: " + referrerUrl + "\t" + referrerClickTime + "\t" + appInstallTime + "\n" + instantExperienceLaunched);

                        if (!referrerUrl.contains("utm_medium=organic")) {
                            preferences.edit().putBoolean(Const.KEY_ORGANIC_, true).apply();
                        }
                        var0.endConnection();
                    } catch (RemoteException e) {
                        DLog.handleException(e);
                    }
                    break;
                case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                    if (BuildConfig.DEBUG) DLog.d("FEATURE_NOT_SUPPORTED");
                    // API не поддерживается текущей версией Google Play
                    break;
                case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                    if (BuildConfig.DEBUG) DLog.d("SERVICE_UNAVAILABLE");
                    // Соединение не может быть установлено
                    break;
            }
        }

        @Override
        public void onInstallReferrerServiceDisconnected() {
            // Попробуйте перезапустите соединение, заново вызвав метод startConnection()
            var0.startConnection(installReferrerStateListener);
        }
    };
}
