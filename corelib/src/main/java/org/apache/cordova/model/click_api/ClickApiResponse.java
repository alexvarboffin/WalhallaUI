package org.apache.cordova.model.click_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClickApiResponse {

    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("headers")
    @Expose
    public List<Object> headers = null;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("contentType")
    @Expose
    public String contentType;
    @SerializedName("uniqueness_cookie")
    @Expose
    public String uniquenessCookie;
    @SerializedName("info")
    @Expose
    public Info info;
    @SerializedName("cookies_ttl")
    @Expose
    public Integer cookiesTtl;
    @SerializedName("cookies")
    @Expose
    public Cookies cookies;
    @SerializedName("log")
    @Expose
    public List<String> log = null;

}