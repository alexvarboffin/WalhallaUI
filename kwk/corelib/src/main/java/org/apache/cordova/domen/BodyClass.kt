package org.apache.cordova.domen

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BodyClass {
    @SerializedName("time_stamp")
    @Expose
    var time_stamp: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    constructor()

    constructor(time_stamp: String?, body: String?, url: String?) {
        this.time_stamp = time_stamp
        this.body = body
        this.url = url
    }
}
