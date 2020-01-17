package com.walhalla.ui;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;


import java.util.Calendar;

public class Module_U {


    private static final String TAG = "@@@";

    public static void aboutDialog(Context context) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //&#169; - html
        String title = "\u00a9 " + year + " " + PublisherConfig.PLAY_GOOGLE_PUB;

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
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "market://search?q=pub:" + PublisherConfig.PLAY_GOOGLE_PUB)));

        } catch (android.content.ActivityNotFoundException anfe) {
            openBrowser(context, "https://play.google.com/store/search?q=pub:"
                    + PublisherConfig.PLAY_GOOGLE_PUB);
        }
    }

    public static void openBrowser(Context context, String data) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
        } catch (ActivityNotFoundException e) {
            //browser not found
        }
    }


    public static void feedback(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + PublisherConfig.FEEDBACK_EMAIL +
                    "?share_subject=" + Uri.encode(context.getPackageName())));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "e-mail client not found", Toast.LENGTH_LONG).show();
        }
    }

    private void composeEmail(Context context, String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void shareText(Context context, String shareBody) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.app_name)));
    }

    public static void shareThisApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey my friend check out this app" +
                "\n https://play.google.com/store/apps/details?id="
                + context.getPackageName() + " \n");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void rateUs(Context context) {
        try {
            String u = String.format(context.getString(R.string.market_rate_url), context.getPackageName());
            Log.i(TAG, "rateUs: " + u);
            Uri uri = Uri.parse(u);
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + context.getPackageName())));
        }
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