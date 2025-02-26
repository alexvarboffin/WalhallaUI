package org.apache.cordova.domen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


// Класс для работы с Firebase
public class Dataset {

    public ScreenType screenType;

    public Dataset(String url) {
        this.a = url;
    }

    public Dataset(ScreenType type, String url) {
        this.screenType = type;
        this.a = url;
    }

//    public boolean isMenu() {
//        return menu;
//    }
//
//    @SerializedName("menu")
//    @Expose
//    private boolean menu;


    @SerializedName("enabled")
    @Expose
    public Boolean enabled; //not support Boolean type

    //true = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    //false = LANDSCAPE

    @SerializedName("portrait")
    @Expose
    public Integer orientation = null;

    @SerializedName("webview_external")
    @Expose
    public boolean webview_external;

    @SerializedName("viewType")
    @Expose
    public String viewType = "app";


    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    @SerializedName("demo")
    @Expose
    private boolean demo;

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }

    @SerializedName("whitelist")
    @Expose
    private String whitelist;

    public Dataset() {
    }

    public Dataset(Boolean isEnabled, String successUrl, Boolean isDemo, String whitelist) {
        this.enabled = isEnabled;
        this.a = successUrl;
        this.demo = isDemo;
        this.whitelist = whitelist;
    }

    @Expose
    @SerializedName("url")
    private String a;

    public void setUrl(String a) {
        this.a = a;
    }

    public String getUrl() {
        return a;
    }

    public String getWhitelist() {
        return whitelist;
    }

    //@DevelopOnly
    @Override
    public String toString() {
        return "Dataset{" +
                "screenType=" + screenType +
                ", enabled=" + enabled +
                ", portrait=" + orientation +
                ", web=" + webview_external +
                ", url='" + a + '\'' +
                ", demo=" + demo +
                ", whitelist='" + whitelist + '\'' +
                '}';
    }

    public boolean isEnabled() {
        return enabled != null && enabled;
    }
}