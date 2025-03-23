package com.walhalla.ui.observer

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.walhalla.ui.R
import com.walhalla.ui.SharedPref.Companion.getInstance

class AgreementObserver(activity: AppCompatActivity, url: String?) : AlertDialog.Builder(activity),
    DefaultLifecycleObserver {
    private val var1 = getInstance(activity)

    init {
        setTitle(activity.getString(R.string.app_name))
        //setIcon(R.mipmap.ic_launcher_round);
        // Set dialog share_message
        setMessage(activity.getString(com.walhalla.shared.R.string.agreement_text)).setCancelable(
            false
        )
            .setPositiveButton(
                activity.getString(android.R.string.yes)
            ) { dialog: DialogInterface, id: Int ->
                var1.licenseAgree(true)
                dialog.cancel()
            }
            //                .setNeutralButton(activity.getString(R.string.action_privacy_policy), (dialog, id) -> {
            //                    Launcher.openBrowser(activity, url);
            //                })

            .setNegativeButton(activity.getString(android.R.string.no)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                activity.finish()
            }
        this.create()
    }

    override fun onCreate(owner: LifecycleOwner) {
        if (!var1.licenseAgree()) {
            this.show()
        }
    }
}
