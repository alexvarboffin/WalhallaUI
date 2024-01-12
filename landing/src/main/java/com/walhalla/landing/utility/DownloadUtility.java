package com.walhalla.landing.utility;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.walhalla.landing.WebViewAppConfig;
import com.walhalla.ui.DLog;

public class DownloadUtility {

    @SuppressLint("ObsoleteSdkInt")
    public static void downloadFile(Context context, String url, String fileName) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                // set download directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                // when downloading music and videos they will be listed in the player
                request.allowScanningByMediaScanner();

                // notify user when download is completed
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // start download
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            } catch (java.lang.SecurityException e) {

                //api 24 need permission


                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                No permission to write to /storage/emulated/0/Download/rules.pdf:
//                 Neither user 10091 nor current process has android.permission.WRITE_EXTERNAL_STORAGE.
                DLog.d("@@" + e.toString() + e.getLocalizedMessage());
            } catch (Exception e) {
                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//                No permission to write to /storage/emulated/0/Download/rules.pdf:
//                 Neither user 10091 nor current process has android.permission.WRITE_EXTERNAL_STORAGE.
                DLog.d("@@" + e.toString() + e.getLocalizedMessage());
            }
        } else {
            try {
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }
            } catch (android.content.ActivityNotFoundException e) {
                // can't start activity
            }
        }
    }


    public static boolean isLinkInternal(String url) {
        for (String rule : WebViewAppConfig.LINKS_OPENED_IN_INTERNAL_WEBVIEW) {
            if (url.contains(rule)) return true;
        }
        return false;
    }

    public static String getFileName(String url) {
        int index = url.indexOf("?");
        if (index > -1) {
            url = url.substring(0, index);
        }
        url = url.toLowerCase();

        index = url.lastIndexOf("/");
        if (index > -1) {
            return url.substring(index + 1);
        } else {
            return Long.toString(System.currentTimeMillis());
        }
    }
}
