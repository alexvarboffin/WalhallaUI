package org.apache.cordova.domen

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KwkResponseNew  //We need empty constructor!
{
    @JvmField
    @SerializedName("client_id")
    @Expose
    var clientId: String? = null

    @SerializedName("session_id")
    @Expose
    var sessionId: String? = null

    @SerializedName("offer_id")
    @Expose
    var offerId: Long? = null

    @JvmField
    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @Keep
    @SerializedName("product")
    @Expose
    var product: String? = null

    override fun toString(): String {
        return "KwkResponseNew{" +
                "clientId='" + clientId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", offerId=" + offerId +
                ", response='" + response + '\'' +
                ", message='" + message + '\'' +
                ", product='" + product + '\'' +
                '}'
    }
}
