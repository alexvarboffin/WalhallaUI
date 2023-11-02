package com.walhalla.phonenumber;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class AppUtils {

    public static void openChromeBrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setPackage("com.android.chrome");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show();
        }
    }
}
