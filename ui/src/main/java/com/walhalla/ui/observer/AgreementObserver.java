package com.walhalla.ui.observer;

import android.app.Activity;
import android.app.AlertDialog;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;


import com.walhalla.ui.R;


/**
 * Created by ponch on 14.03.17.
 */

public class AgreementObserver extends AlertDialog.Builder
        implements LifecycleObserver {

    private static final String KEY_AGREE = "license_ok";
    private final SharedPreferences mSharedPreferences;


    public AgreementObserver(final Activity activity, String url) {
        super(activity);

        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        setTitle(activity.getString(R.string.app_name));
        setIcon(R.mipmap.ic_launcher_round);
        // Set dialog share_message
        setMessage(activity.getString(R.string.agreement_text)).setCancelable(false)
                .setPositiveButton(activity.getString(android.R.string.yes),
                        (dialog, id) -> {
                            licenseAgree(true);
                            dialog.cancel();
                        })

//                .setNeutralButton(activity.getString(R.string.action_privacy_policy), (dialog, id) -> {
//                    Module_U.openBrowser(activity, url);
//                })

                .setNegativeButton(activity.getString(android.R.string.no), (dialog, id) -> {

                    dialog.cancel();
                    activity.finish();
                    //System.exit(0);
                });
        this.create();
    }

    public boolean licenseAgree() {
        return mSharedPreferences.getBoolean(KEY_AGREE, false);
    }


    public void licenseAgree(boolean b) {
        mSharedPreferences.edit().putBoolean(KEY_AGREE, b).apply();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        if (!licenseAgree()) {
            this.show();
        }
    }
}
