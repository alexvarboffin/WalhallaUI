package com.walhalla.ui.observer;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import androidx.preference.PreferenceManager;

import com.walhalla.core.SharedPref;
import com.walhalla.ui.R;


public class AgreementObserver extends AlertDialog.Builder implements DefaultLifecycleObserver {


    private final SharedPref var1;

    public AgreementObserver(final AppCompatActivity activity, String url) {
        super(activity);

        var1 = SharedPref.getInstance(activity);

        setTitle(activity.getString(R.string.app_name));
        //setIcon(R.mipmap.ic_launcher_round);
        // Set dialog share_message
        setMessage(activity.getString(R.string.agreement_text)).setCancelable(false)
                .setPositiveButton(activity.getString(android.R.string.yes),
                        (dialog, id) -> {
                            var1.licenseAgree(true);
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

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        if (!var1.licenseAgree()) {
            this.show();
        }
    }
}
