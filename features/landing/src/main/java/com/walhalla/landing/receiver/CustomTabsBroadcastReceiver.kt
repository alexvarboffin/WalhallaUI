package com.walhalla.landing.receiver

import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomTabsBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_COPY_URL == action) {
                val url = intent.dataString
                Toast.makeText(context, "Copy link pressed. URL = $url", Toast.LENGTH_LONG).show()
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", url)
                clipboard.setPrimaryClip(clip)
            } else if (ACTION_ITEM_2 == action) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            } else if (ACTION_PRIVACY == action) {
                //AboutBox.showPolicy(context);
            }
        }
    }
    //    public void toast(String msg) {
    //        View coordinatorLayout = cfindViewById(android.R.id.content);
    //        if (coordinatorLayout != null) {
    //            Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    //        }
    //    }

    companion object {
        const val ACTION_ITEM_2: String = "action.item2"
        @JvmField
        var ACTION_COPY_URL: String = "action.copy"
        const val ACTION_PRIVACY: String = "action.privacy"
    }
}
