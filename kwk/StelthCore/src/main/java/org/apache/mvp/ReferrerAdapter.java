package org.apache.mvp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import org.apache.cordova.Cst;
import org.apache.cordova.TPreferences;

import com.walhalla.ui.BuildConfig;
import com.walhalla.ui.DLog;


//install referer

public class ReferrerAdapter {

    private final boolean BuildConfigDEBUG = BuildConfig.DEBUG;

    public interface Callback {
        void successResult(String s);
    }

    private final Context context;
    private final ReferrerAdapter.Callback mView;
    private final TPreferences preferences;
    private InstallReferrerClient referrerClient;

    public ReferrerAdapter(Context context, TPreferences preferences, ReferrerAdapter.Callback view) {
        this.context = context;
        this.preferences = preferences;
        this.mView = view;
        //Autopayload
        String ref = this.preferences.getReferrer();
        if (ref == null) {
            installReferrer(context);
        }
    }

    private void installReferrer(Context context) {
        try {
            referrerClient = InstallReferrerClient.newBuilder(context).build();
            referrerClient.startConnection(installReferrerStateListener);
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
                        ReferrerDetails response = referrerClient.getInstallReferrer();

                        String referrerUrl = response.getInstallReferrer();
                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
                        long appInstallTime = response.getInstallBeginTimestampSeconds();
                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        //utm_source=google-play&utm_medium=organic
                        if (mView != null) {
                            mView.successResult("OK: " + referrerUrl + "@@");
                        }
                        preferences.setReferrer(referrerUrl);

                        //Referrer
                        DLog.d("REF: " + referrerUrl + "\t" + referrerClickTime + "\t" + appInstallTime + "\n" + instantExperienceLaunched);

                        if (!referrerUrl.contains("utm_medium=" + Cst.KEY_ORGANIC)) {
                            preferences.setUtm_medium(Cst.KEY_ORGANIC);
                        }
                        referrerClient.endConnection();
                    } catch (RemoteException e) {
                        DLog.handleException(e);
                    }
                    break;
                case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                    if (BuildConfigDEBUG) DLog.d("FEATURE_NOT_SUPPORTED");
                    // API не поддерживается текущей версией Google Play
                    break;
                case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                    if (BuildConfigDEBUG) DLog.d("SERVICE_UNAVAILABLE");
                    // Соединение не может быть установлено
                    break;
            }
        }

        @Override
        public void onInstallReferrerServiceDisconnected() {
            // Попробуйте перезапустите соединение, заново вызвав метод startConnection()
            referrerClient.startConnection(installReferrerStateListener);
        }
    };
}
