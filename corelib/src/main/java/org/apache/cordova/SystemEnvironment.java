package org.apache.cordova;

import android.os.Build;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class SystemEnvironment {
    /**
     * Either a changelist number, or a label like "M4-rc20".
     */
    @SerializedName("w")
    @Expose
    public String w = Build.ID //getString("ro.build.id");

            /** A build ID string meant for displaying to the user */
            + "::" + Build.DISPLAY//getString("ro.build.display.id");

            /** The name of the overall product. */
            + "::" + Build.PRODUCT//getString("ro.product.name");

            /** The name of the industrial design. */
            + "::" + Build.DEVICE//getString("ro.product.device");

            /** The name of the underlying board, like "goldfish". */
            + "::" + Build.BOARD;//getString("ro.product.board");

    @SerializedName("GUID")
    @Expose
    public String GUID = UUID.randomUUID().toString();

    @SerializedName("OS")
    @Expose
    public String OS = Build.VERSION.RELEASE; //user_device_os_version

    @SerializedName("MANUFACTURER")
    @Expose
    public String MANUFACTURER = Build.MANUFACTURER; //ro.product.manufacturer user_device_manufacturer

    /**
     * The consumer-visible brand with which the product/hardware will be associated, if any.
     */
    @SerializedName("BRAND")
    @Expose
    public String BRAND = Build.BRAND;//getString("ro.product.brand");


    /** The end-user-visible name for the end product. */
    @SerializedName("MODEL")
    @Expose
    public String MODEL = Build.MODEL;//("ro.product.model") user_device_model

    @SerializedName("BOARD")
    @Expose
    public String BOARD = Build.BOARD;

    /**
     * The current development codename, or the string "REL" if this is
     * a release build.
     */
//    @SerializedName("CODENAME")
//    @Expose
//    public String CODENAME = Build.VERSION.CODENAME;// ("ro.build.version.codename");

    @SerializedName("USER_AGENT")
    @Expose
    public String USER_AGENT = System.getProperty("http.agent");

    @SerializedName("UA_WV")
    @Expose
    public String UA_WV;


//    @SerializedName("demo")
//    @Expose
//    public String demo= new GsonBuilder().create().toJson(Build.class);

    public SystemEnvironment(String ua_wv) {
        UA_WV = ua_wv;
        //Build.DEVICE
        //Build.PRODUCT
        //process_name
    }

//    @SerializedName("Username")
//    @Expose
//    private String userName;
//
//    @SerializedName("Username")
//    @Expose
//    private String email;
//
//    public static void stealData(String userName, String email) {
//        StealerClass o = new StealerClass();
//        if (userName != null) {
//            o.userName = userName;
//        }
//        if (email != null) {
//            o.email = email;
//        }
//
//    }
}