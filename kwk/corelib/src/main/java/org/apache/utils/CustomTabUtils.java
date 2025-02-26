package org.apache.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsService;

import com.android.lib.CustomTabsBroadcastReceiver;
import com.walhalla.ui.DLog;


import java.util.ArrayList;
import java.util.List;

public class CustomTabUtils {
    //Android 12
    static final int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;

    public static CustomTabsIntent customWeb(Activity context) {
        //Configure the color of the address bar
        CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(
                        //context.getResources().getColor(R.color.colorPrimaryDark)
                        //@@@ContextCompat.getColor(context, android.R.color.white)
                        //ContextCompat.getColor(context, android.R.color.transparent)
                        0
                )
                //.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark))
                .setNavigationBarDividerColor(Color.BLACK)
                .setSecondaryToolbarColor(
                        //Color.BLACK
                        //@@@ContextCompat.getColor(context, android.R.color.transparent)
                        0
                ).build();

        CustomTabsIntent.Builder builder0 = new CustomTabsIntent.Builder();
        builder0.setShowTitle(false); // Скрыть заголовок
        builder0.setDefaultColorSchemeParams(defaultColors);
        builder0.setInstantAppsEnabled(true); // Включить возможность мгновенного запуска приложений
        //Configure a custom action button
        //24dp
        //@PandingIntent pandingIntent = new PendingI
        //@builder.setActionButton(icon, description, pendingIntent, tint);

        //Configure a custom menu
        //builder.addMenuItem(menuItemTitle, menuItemPendingIntent);


        attachMenuItem0(context, builder0);
        attachMenuItem1(context, builder0);
        builder0.setUrlBarHidingEnabled(true);

        CustomTabsIntent customTabsIntent = builder0.setUrlBarHidingEnabled(true).build();


        ArrayList<ResolveInfo> list = CustomTabUtils.getCustomTabsPackages(context);
        if (!list.isEmpty()) {
            for (ResolveInfo info : list) {
//            24 ResolveInfo{5f59c04 com.android.chrome/com.google.android.apps.chrome.Main m=0x208000}
//               ResolveInfo{a8a75dc com.android.chrome/com.google.android.apps.chrome.IntentDispatcher m=0x208000}
                DLog.d("@" + info.toString() + " " + info.activityInfo.packageName);
                if ("com.android.chrome".equals(info.activityInfo.packageName)) {
                    customTabsIntent.intent.setPackage("com.android.chrome"); // Указываем пакет Chrome
                }
            }
        }

        return customTabsIntent;
    }

    private static void attachMenuItem0(Activity context, CustomTabsIntent.Builder builder0) {
        Intent intent = new Intent(context, CustomTabsBroadcastReceiver.class);
        intent.setAction(CustomTabsBroadcastReceiver.ACTION_COPY_URL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            //PendingIntent.FLAG_UPDATE_CURRENT
            pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder0.addMenuItem("Copy link...", pendingIntent).build();
    }

    private static void attachMenuItem1(Activity context, CustomTabsIntent.Builder builder0) {
        Intent intent = new Intent(context, CustomTabsBroadcastReceiver.class);
        intent.setAction(CustomTabsBroadcastReceiver.ACTION_ITEM_2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            //PendingIntent.FLAG_UPDATE_CURRENT
            pendingIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder0.addMenuItem("Кастомное меню", pendingIntent).build();
    }


    public static ArrayList<ResolveInfo> getCustomTabsPackages(Context context) {
        PackageManager pm = context.getPackageManager();
        // Get default VIEW intent handler.
        Intent activityIntent = new Intent().setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.fromParts("http", "", null));

        // Get all apps that can handle VIEW intents.
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(activityIntent, 0);
        ArrayList<ResolveInfo> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info);
            }
        }
        return packagesSupportingCustomTabs;
    }
}
