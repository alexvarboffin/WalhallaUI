package org.apache.cordova.domen;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class BodyClass {

    @SerializedName("time_stamp")
    @Expose
    public String time_stamp;
    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("url")
    @Expose
    public String url;

    public BodyClass() {
    }

    public BodyClass(String time_stamp, String body, String url) {
        this.time_stamp = time_stamp;
        this.body = body;
        this.url = url;
    }
}
