package org.apache.cordova.domen

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

// Класс для работы с Firebase
class Dataset {
    var screenType: ScreenType? = null

    constructor(url: String?) {
        this.url = url
    }

    constructor(type: ScreenType?, url: String?) {
        this.screenType = type
        this.url = url
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
    var enabled: Boolean? = null //not support Boolean type

    //true = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    //false = LANDSCAPE
    @SerializedName("portrait")
    @Expose
    var orientation: Int? = null

    @SerializedName("webview_external")
    @Expose
    var webview_external: Boolean = false

    @SerializedName("viewType")
    @Expose
    var viewType: String = "app"


    @SerializedName("demo")
    @Expose
    var isDemo: Boolean = false

    @SerializedName("whitelist")
    @Expose
    var whitelist: String? = null

    constructor()

    constructor(isEnabled: Boolean?, successUrl: String?, isDemo: Boolean, whitelist: String?) {
        this.enabled = isEnabled
        this.url = successUrl
        this.isDemo = isDemo
        this.whitelist = whitelist
    }

    @Expose
    @SerializedName("url")
    var url: String? = null

    //@DevelopOnly
    override fun toString(): String {
        return "Dataset{" +
                "screenType=" + screenType +
                ", enabled=" + enabled +
                ", portrait=" + orientation +
                ", web=" + webview_external +
                ", url='" + url + '\'' +
                ", demo=" + isDemo +
                ", whitelist='" + whitelist + '\'' +
                '}'
    }

    fun isEnabled(): Boolean {
        return enabled != null && enabled as Boolean
    }
}