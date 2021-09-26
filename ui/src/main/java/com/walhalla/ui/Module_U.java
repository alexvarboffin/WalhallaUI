package com.walhalla.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.UConst;
import com.github.javiersantos.appupdater.AppUpdater;

import java.util.Calendar;

public class Module_U {

    private static final String PKG_NAME_VENDING = "com.android.vending";


    //Show me the magik...getInstallerPackageName

    private boolean isFromGooglePlay(Context context, String pName) {
        PackageManager packageManager = context.getPackageManager();
        String installPM = packageManager.getInstallerPackageName(pName);
        if (installPM == null) {
            // Definitely not from Google Play
            return false;
        } else if (PKG_NAME_VENDING.equals(installPM)
                || "com.com.google.android.feedback".equals(installPM)
        ) {
            // Installed from the Google Play
            return true;
        }
        return false;
    }


    public static void aboutDialog(Context context) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //&#169; - html
        String title = "\u00a9 " + year + " " + context.getString(R.string.play_google_pub);

        View mView = LayoutInflater.from(context).inflate(R.layout.about, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(null)
                .setCancelable(true)
                .setIcon(null)
                //.setPositiveButton(button, null)
                .setView(mView)
                .create();
        mView.setOnClickListener(v -> dialog.dismiss());
        TextView textView = mView.findViewById(R.id.about_version);
        textView.setText(DLog.getAppVersion(context));
        ((TextView) mView.findViewById(R.id.about_copyright)).setText(title);
        dialog.show();
    }

    /**
     * more_apps_link = "https://play.google.com/store/apps/dev?id=5700313618786177705"
     */
    public static void moreApp(Context context) {
        final String pub = context.getString(R.string.play_google_pub);
        try {
            context.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + pub)));

        } catch (android.content.ActivityNotFoundException anfe) {
            openBrowser(context, "https://play.google.com/store/search?q=pub:" + pub);
        }
    }

    public static void openMarket(Context context, String packageName) {
        try {
            Uri uri = Uri.parse(UConst.MARKET_CONSTANT + packageName);
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (android.content.ActivityNotFoundException anfe) {
            openBrowser(context, UConst.GOOGLE_PLAY_CONSTANT + packageName);
        }
    }

    public static void rateUs(Context context) {
        String packageName = context.getPackageName();
        openMarket(context, packageName);
    }

    public static void openBrowser(Context context, String data) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show();
        }
    }


    public static void feedback(Context context) {

        try {
            String subject = Uri.encode(context.getPackageName()) + "_" + DLog.getAppVersion(context);
            subject = subject.replace("com.walhalla.", "");
            DLog.d(subject + "\t" + context.getString(R.string.publisher_feedback_email));

//            Intent intent = new Intent(Intent.ACTION_SENDTO);
//            intent.setData(Uri.parse("mailto:" + PublisherConfig.FEEDBACK_EMAIL +
//                    "?share_subject=" + Uri.encode(context.getPackageName())));
//
//            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);

            composeEmail(context, new String[]{context.getString(R.string.publisher_feedback_email)}, subject);
        } catch (Exception e) {
            DLog.handleException(e);
            Toast.makeText(context, "e-mail client not found", Toast.LENGTH_LONG).show();
        }
    }

    private static void composeEmail(Context context, String[] addresses, String subject) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent != null && intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            DLog.handleException(e);
            Toast.makeText(context, "e-mail client not found", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareText(Context context, String shareBody) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        intent.putExtra("com.pinterest.EXTRA_DESCRIPTION", shareBody);
        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.app_name)));
    }

    public static void shareThisApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT,
                "Hey my friend check out this app"
                        + (char) 10 + UConst.GOOGLE_PLAY_CONSTANT
                        + context.getPackageName() + (char) 10
        );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void checkUpdate(Context context) {
        AppUpdater appUpdater = new AppUpdater(context)
                .setContentOnUpdateAvailable(R.string.update_available)
                .setCancelable(false)
                .setButtonDoNotShowAgain("")
                .setButtonUpdate(R.string.update_now)
                .setButtonDismiss(R.string.update_later)
                .setTitleOnUpdateNotAvailable(R.string.update_not_available)
                .setContentOnUpdateNotAvailable(R.string.update_check_later);
        appUpdater.start();
    }
}