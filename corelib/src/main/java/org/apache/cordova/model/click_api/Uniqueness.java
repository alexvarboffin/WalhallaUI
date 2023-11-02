package org.apache.cordova.model.click_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Uniqueness {

    @SerializedName("campaign")
    @Expose
    public Boolean campaign;
    @SerializedName("stream")
    @Expose
    public Boolean stream;
    @SerializedName("global")
    @Expose
    public Boolean global;

}
