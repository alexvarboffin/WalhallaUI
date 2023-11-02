package org.apache.cordova.v70.utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ActivityUtils {
    public static void startEmailActivity(Activity activity, String email, String subject, String text) {
        try {
            String builder = "mailto:" + email;

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(builder));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "can't start activity: " + text, Toast.LENGTH_LONG).show();
        }
    }


    public static void startCallActivity(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "can't start activity: " + url, Toast.LENGTH_LONG).show();
        }
    }


    public static void startSmsActivity(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "can't start activity: " + url, Toast.LENGTH_LONG).show();
        }
    }


    public static void startMapSearchActivity(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "can't start activity: " + url, Toast.LENGTH_LONG).show();
        }
    }

    public static void startMapYandex(Activity context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "can't start activity: " + url, Toast.LENGTH_LONG).show();
        }
    }

    public static void startyandexnavi(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "can't start activity: " + url, Toast.LENGTH_LONG).show();
        }
    }

    public static void startShareActivity(Activity activity, String subject, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "can't start activity: " + text, Toast.LENGTH_LONG).show();
        }
    }


}
