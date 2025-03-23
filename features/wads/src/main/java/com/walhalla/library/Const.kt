package com.walhalla.library

import com.google.android.gms.ads.AdRequest

object Const {
    //ca-app-pub-3940256099942544~3347511713 test app id
    @JvmField
    var TEST_BANNER_ADS: String = "ca-app-pub-3940256099942544/6300978111"
    @JvmField
    var TEST_INTERSTITIAL_ID: String = "ca-app-pub-3940256099942544/1033173712"
    @JvmField
    var TEST_REWARDED_VIDEO_ID: String = "ca-app-pub-3940256099942544/5224354917"

    var TEST_NATIVE_ID: String = "ca-app-pub-3940256099942544/2247696110"


    @JvmStatic
    fun testDevices(): Array<String> {
        return arrayOf(
            AdRequest.DEVICE_ID_EMULATOR, "169F25005B1A084E22E3FE83105E9D81"
        )
    }
}
