package com.walhalla.abcsharedlib;

public class SharedNetwork {

    public static final String PACKAGE_LIKEE = "video.like";
    public static final String PACKAGE_TWITTER = "com.twitter.android";
    public static final String PACKAGE_TWITTER_LITE = "com.twitter.android.lite";

    public static final String PACKAGE_INSTAGRAM = "com.instagram.android";
    public static final String PACKAGE_WHATSAPP = "com.whatsapp";
    public static final String PACKAGE_VIBER = "com.viber.voip";

    public static final String PACKAGE_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_TIKTOK_M_PACKAGE = "com.zhiliaoapp.musically";
    public static final String PACKAGE_TIKTOK_T_PACKAGE = "com.ss.android.ugc.trill";

    public static final String PACKAGE_PINTEREST = "com.pinterest";


    public static String[] twitter() {
        return new String[]{
                SharedNetwork.PACKAGE_TWITTER,
                SharedNetwork.PACKAGE_TWITTER_LITE
        };
    }

    public static String[] pinterest() {
        return new String[]{
                SharedNetwork.PACKAGE_PINTEREST
        };
    }
}