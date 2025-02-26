package org.apache.cordova;

public class Cst {


    public static final String KEY_ORGANIC = "organic";

    //public static final String KEY_SIGNED = "pixel";


    public static class UrlBuilder{
        public static final String KEY_COUNTRY = "country";
        public static final String KEY_APP = "app";


        //extended
        //new kwk extended param
        public static final String key_pref_param_clid = "key_client_id";//NEW param client_id
    }

    public static final String KEY_DEVICE = "device";

    public static final String KEY_AF_ID = "af_id"; //"apps_flyer_id"
    public static final String KEY_BID = "bid";

    //AdsBridge
    //&device="device"
    public static final String KEY_DEVICE_MODEL = "devicemodel"; //or "model"
    public static final String gclid = "gclid";
    public static final String campid = "campid";
    public static final String adid = "adid";
    public static final String keyword = "keyword";
    public static final String creative = "creative";
    public static final String adposition = "adposition";
    public static final String matchtype = "matchtype";
    public static final String network = "network";
    public static final String placement = "placement";
    public static final String target = "target";
    public static final String zoneid = "zoneid";


    //var1
    //public static String param_advertisingId = "usserid"; // - рекламный айди устройства
    public static String param_advertisingId = "gaid";// - рекламный айди устройства

    //subid -> app_name
    //sourceid --> | utm_source=google-play&utm_medium=organic

    //"platform":"Android"
    //


    //Firebase token
    public static final String PREF_KEY_FBM_TKN = "key_fb_tkn";



    public static class Firebase {
        public static final String KEY_SIM_CARD = "sim_card";
        public static final String KEY_VPN = "vpn";
        public static final String KEY_COUNTRY = "country";
        public static final String KEY_LANG = "lang";
        public static final String KEY_LIP = "LIP";
        public static String KEY_DEEP_LINK = "deep_link";
        public static String KEY_referer = "referer";
    }
}
