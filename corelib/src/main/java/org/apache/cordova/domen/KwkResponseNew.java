package org.apache.cordova.domen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KwkResponseNew {

    public KwkResponseNew() {} //We need empty constructor!

    @SerializedName("client_id")
    @Expose
    public String clientId;
    @SerializedName("session_id")
    @Expose
    public String sessionId;
    @SerializedName("offer_id")
    @Expose
    public Long offerId;
    @SerializedName("response")
    @Expose
    public String response;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("product")
    @Expose
    public String product;

    @Override
    public String toString() {
        return "KwkResponseNew{" +
                "clientId='" + clientId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", offerId=" + offerId +
                ", response='" + response + '\'' +
                ", message='" + message + '\'' +
                ", product='" + product + '\'' +
                '}';
    }
}
