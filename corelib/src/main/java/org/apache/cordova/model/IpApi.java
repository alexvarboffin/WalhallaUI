package org.apache.cordova.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * http://ip-api.com/json/
 */
@Keep
public class IpApi {

    public IpApi() {
        //keep
    }

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("countryCode")
    @Expose
    public String countryCode;
    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("regionName")
    @Expose
    public String regionName;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("zip")
    @Expose
    public String zip;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("isp")
    @Expose
    public String isp;
    @SerializedName("org")
    @Expose
    public String org;
    @SerializedName("as")
    @Expose
    public String as;
    @SerializedName("query")
    @Expose
    public String query;

    @Override
    public String toString() {
        return "IpApi{" +
                "status='" + status + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", region='" + region + '\'' +
                ", regionName='" + regionName + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", isp='" + isp + '\'' +
                ", org='" + org + '\'' +
                ", as='" + as + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
