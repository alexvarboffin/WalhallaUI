package com.walhalla.landing.loader;

import android.content.Context;

import com.google.gson.Gson;
import com.walhalla.landing.config.Cfg;

import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {

//    public boolean splash_screen_enabled;
//    public boolean toolbar_enabled;
//    public boolean check_connection;
//    public boolean progress_enabled;
//    public boolean swipe_enabled;

    public static Cfg loadConfig(Context context) {
        String json = loadJSONFromAsset(context, "Config.json");
        Config mm = new Gson().fromJson(json, Config.class);
        com.walhalla.landing.config.Cfg cfg;
        if (mm != null) {
            cfg = new com.walhalla.landing.config.Cfg.Builder()
                    .setToolbarEnabled(mm.toolbar_enabled)
                    .setCounterTimeMs(2400)
                    .setSplashScreenEnabled(mm.splash_screen_enabled)
                    .setCheckConnection(mm.isCheckConnection())
                    .setSwipeEnabled(mm.swipe_enabled)
                    .setProgressEnabled(mm.progress_enabled)
                    .build();
        } else {
            cfg = null;//new Cfg();
        }
        return cfg;
    }

//    public static Config loadConfig(Context context) {
//        String json = loadJSONFromAsset(context, "Config.json");
//        Config mm = new Gson().fromJson(json, Config.class);
//        return mm;
//    }

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}