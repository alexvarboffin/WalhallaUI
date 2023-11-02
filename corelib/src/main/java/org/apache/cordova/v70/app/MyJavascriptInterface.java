package org.apache.cordova.v70.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.webkit.JavascriptInterface;

import androidx.appcompat.UWView;
import androidx.appcompat.app.AlertDialog;

import com.walhalla.ui.DLog;


public class MyJavascriptInterface {

    private final UWView webView;
    private final Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    public MyJavascriptInterface(Context c, UWView webView) {
        this.webView = webView;
        mContext = c;
    }
    @JavascriptInterface
    public void openBrowser(String data) {
//
//        try {
//            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PRIVACY_POLICY)));
//        } catch (ActivityNotFoundException e) {
//            //browser not found
//        }
    }


    @JavascriptInterface
    public void log(String data) {
        DLog.d("log: " + data);
    }

    @JavascriptInterface   // must be added for API 17 or higher
    public void showToast(String toast) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void finish() {
//            PackageHelper.removePackage(str);
//            InjectionActivity.this.finish();
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void showConfirmationDialog(final String callbackFunction, final String data, String title,
                                       String message) {

        Dialog.OnClickListener positiveListener = (dialog, which) ->
                webView.loadUrl("javascript:" + callbackFunction + "('" + data + "', true)");
        Dialog.OnClickListener negativeListener = (dialog, which) ->
                webView.loadUrl("javascript:" + callbackFunction + "('" + data + "', false)");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setMessage(message).setPositiveButton(
                android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener).setCancelable(false);
        builder.create().show();
    }
}
