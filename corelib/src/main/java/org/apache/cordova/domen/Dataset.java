package org.apache.cordova.domen;


import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.cordova.ScreenType;

public class Dataset {

    public ScreenType screenType;

    public Dataset(String url) {
        this.url = url;
    }

    public Dataset(ScreenType type, String url) {
        this.screenType = type;
        this.url = url;
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


    @SerializedName("url")
    @Expose
    public String url;

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    @SerializedName("demo")
    @Expose
    private boolean demo;
    @SerializedName("whitelist")
    @Expose
    private String whitelist;

    public Dataset() {
    }

    public Dataset(Boolean isEnabled, String successUrl, Boolean isDemo, String whitelist) {
        this.enabled = isEnabled;
        this.url = successUrl;
        this.demo = isDemo;
        this.whitelist = whitelist;
    }

    public String getUrl() {
        return url;
    }

    public String getWhitelist() {
        return whitelist;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "screenType=" + screenType +
                ", enabled=" + enabled +
                ", portrait=" + orientation +
                ", web=" + webview_external +
                ", url='" + url + '\'' +
                ", demo=" + demo +
                ", whitelist='" + whitelist + '\'' +
                '}';
    }

    public boolean isEnabled() {
        return enabled != null && enabled;
    }
}
