package com.walhalla.landing.loader

import android.content.Context
import com.google.gson.Gson
import com.walhalla.landing.config.Cfg
import java.io.IOException

object ConfigLoader {
    //    public boolean splash_screen_enabled;
    //    public boolean toolbar_enabled;
    //    public boolean check_connection;
    //    public boolean progress_enabled;
    //    public boolean swipe_enabled;
    fun loadConfig(context: Context): Cfg? {
        val json = loadJSONFromAsset(context, "Config.json")
        val mm = Gson().fromJson<Config?>(json, Config::class.java)
        val cfg: Cfg?
        if (mm != null) {
            cfg = Cfg.Builder()
                .setToolbarEnabled(mm.toolbar_enabled)
                .setCounterTimeMs(2400)
                .setSplashScreenEnabled(mm.splash_screen_enabled)
                .setCheckConnection(mm.isCheckConnection)
                .setSwipeEnabled(mm.swipe_enabled)
                .setProgressEnabled(mm.progress_enabled)
                .build()
        } else {
            cfg = null //new Cfg();
        }
        return cfg
    }

    //    public static Config loadConfig(Context context) {
    //        String json = loadJSONFromAsset(context, "Config.json");
    //        Config mm = new Gson().fromJson(json, Config.class);
    //        return mm;
    //    }
    private fun loadJSONFromAsset(context: Context, fileName: String?): String? {
        var json: String? = null
        try {
            val `is` = context.getAssets().open(fileName!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}