package org.apache.cordova.base;

public class AppConfig {

    public final String APPSFLYER_DEV_KEY;
    public final String MYTRACKER_KEY;
    public final String APP_METRICA_KEY;
    public final boolean FACEBOOK_ENABLED;
    public final String ONESIGNAL_APP_ID;

    public AppConfig(
            String API_key,
            boolean FACEBOOK,
            String ONESIGNAL,
            String MYTRACKER_KEY,
            String APPSFLYER_DEV_KEY

    ) {
        this.APP_METRICA_KEY = API_key;
        this.FACEBOOK_ENABLED = FACEBOOK;
        this.ONESIGNAL_APP_ID = ONESIGNAL;
        this.MYTRACKER_KEY = MYTRACKER_KEY;
        this.APPSFLYER_DEV_KEY = APPSFLYER_DEV_KEY;
    }
}