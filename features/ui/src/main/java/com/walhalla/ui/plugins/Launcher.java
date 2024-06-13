package com.walhalla.ui.plugins;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.walhalla.ui.UConst;

public class Launcher {

    public static void openMarketApp(Context context, final String packageName) {
        try {
            Uri uri = Uri.parse(UConst.MARKET_CONSTANT + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(Module_U.PKG_NAME_VENDING);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
            } catch (ActivityNotFoundException a) {
                openBrowser(context, UConst.GOOGLE_PLAY_CONSTANT + packageName);
            }
        }
    }

    public static void rateUs(Context context) {
        String packageName = context.getPackageName();
        openMarketApp(context, packageName);
    }

    public static void openBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Browser not found", Toast.LENGTH_SHORT).show();
        }
    }
}
