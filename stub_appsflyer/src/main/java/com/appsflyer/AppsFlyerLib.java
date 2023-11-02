package com.appsflyer;

import android.app.Application;
import android.content.Context;

public class AppsFlyerLib {
    public static AppsFlyerLib getInstance() {
        return new AppsFlyerLib();
    }

    public void init(String appsflyer_dev_key, AppsFlyerConversionListener conversionListener, Application application) {
    }

    public String getAppsFlyerUID(Context context) {
        return "";
    }
}
