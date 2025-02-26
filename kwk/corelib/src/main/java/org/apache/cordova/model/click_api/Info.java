package org.apache.cordova.model.click_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("campaign_id")
    @Expose
    public Integer campaignId;
    @SerializedName("stream_id")
    @Expose
    public Integer streamId;
    @SerializedName("landing_id")
    @Expose
    public Integer landingId;
    @SerializedName("sub_id")
    @Expose
    public String subId;
    @SerializedName("is_bot")
    @Expose
    public Boolean isBot;
    @SerializedName("offer_id")
    @Expose
    public String offerId;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("uniqueness")
    @Expose
    public Uniqueness uniqueness;

}