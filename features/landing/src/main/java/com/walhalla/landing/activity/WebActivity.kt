package com.walhalla.landing.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.walhalla.landing.R

import com.walhalla.ui.R as uiR

import com.walhalla.landing.base.UWVlayout.UWVlayoutCallback
import com.walhalla.landing.permissionrequest.ConfirmationDialogFragment
import com.walhalla.ui.plugins.Launcher.openBrowser
import com.walhalla.webview.ChromeView
import com.walhalla.webview.ReceivedError

abstract class WebActivity : AppCompatActivity(), ChromeView, UWVlayoutCallback,
    ConfirmationDialogFragment.Listener {
    //private CustomTabsIntent customTabsIntent;


    val handler = Handler(Looper.getMainLooper())
    var presenter0 = WPresenterImpl(handler, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.customTabsIntent = CustomTabUtils.customWeb(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter0.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun openBrowser(url: String) {
        openBrowser(this, url)
    }

    //    @Override
    //    public void openOauth2(Activity context, String url) {
    //        try {
    ////        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    ////        Bundle bundle = new Bundle();
    ////        bundle.putString("goz_ad_click", "1");
    ////        mFirebaseAnalytics.logEvent("in_offer_open_click", bundle);
    //            customTabsIntent.launchUrl(context, Uri.parse(url));
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //        }
    //    }
    open fun backPressedToast() {
        var coordinatorLayout = findViewById<View>(R.id.coordinatorLayout)
        if (coordinatorLayout != null) {
            coordinatorLayout = findViewById(android.R.id.content)
        }
        if (coordinatorLayout != null) {
            Snackbar.make(
                coordinatorLayout,
                getString(uiR.string.press_again_to_exit),
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }
    }

    override fun webClientError(failure: ReceivedError) {
    }


    //============================== WEBVIEW ===================
    override fun closeApplication() {
        finish()
    }

    override fun copyToClipboard(url: String) {
        val context: Activity = this
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", "" + url)
        clipboard.setPrimaryClip(clip)
        val tmp = String.format(context.getString(R.string.data_to_clipboard), url)

//        Toasty.custom(context, tmp,
//                ComV19.getDrawable(context, R.drawable.ic_info),
//                ContextCompat.getColor(context, R.color.colorPrimaryDark),
//                ContextCompat.getColor(context, R.color.white), Toasty.LENGTH_SHORT, true, true).show();

        //Toast.makeText(context, tmp, Toast.LENGTH_SHORT).show();

//        View coordinatorLayout = findViewById(com.walhalla.webview.R.id.coordinatorLayout);
//        if (coordinatorLayout != null) {
        val coordinatorLayout = context.findViewById<View>(android.R.id.content)
        //        }
        if (coordinatorLayout != null) {
            Snackbar.make(coordinatorLayout, tmp, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onConfirmation(allowed: Boolean, resources: Array<String>) {
        presenter0.onConfirmation__(allowed, resources)
    }
}