package com.walhalla.landing.loader;

import android.content.Context;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {

    public static Config loadConfig(Context context) {
        String json = loadJSONFromAsset(context, "Config.json");
        return new Gson().fromJson(json, Config.class);
    }

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