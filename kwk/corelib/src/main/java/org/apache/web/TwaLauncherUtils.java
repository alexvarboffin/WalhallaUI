package org.apache.web;

import android.app.Activity;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;

import com.google.androidbrowserhelper.trusted.TwaLauncher;


import org.apache.cordova.R;

import java.util.ArrayList;
import java.util.List;

public class TwaLauncherUtils {


    private final List<TwaLauncher> launchers0;
    private final int toolbarColor;
    private final Activity context;

    public TwaLauncherUtils(Activity context) {
        this.launchers0 = new ArrayList<>();
        this.context = context;
        toolbarColor = context.getResources().getColor(R.color.colorPrimaryDark);
    }


    public void launchUrl(String url) {

        //Configure the color of the address bar
        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(toolbarColor)
                //.setNavigationBarColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .build();
        TrustedWebActivityIntentBuilder toolbarColor = new TrustedWebActivityIntentBuilder(
                Uri.parse(url))
                .setDefaultColorSchemeParams(params);
        //.setNavigationBarColor(Color.WHITE) //deprecated
        //.setToolbarColor(Color.WHITE); //deprecated

        TwaLauncher twaLauncher = new TwaLauncher(context);
        twaLauncher.launch(toolbarColor,
                null,
                null,
                new Runnable() {
                    @Override
                    public void run() {
                        //completetionCallback
                    }
                }
        );
        this.launchers0.add(twaLauncher);
    }
}
