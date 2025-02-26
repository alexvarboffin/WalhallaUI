package com.walhalla.abcsharedlib

object SharedNetwork {
    fun twitter(): Array<String> {
        return arrayOf(
            Package.TWITTER, Package.TWITTER_LITE
        )
    }

    fun pinterest(): Array<String> {
        return arrayOf(
            Package.PINTEREST, Package.PINTERESTLITE
        )
    }

    object Package {
        const val LIKEE: String = "video.like"
        const val TWITTER: String = "com.twitter.android"
        const val TWITTER_LITE: String = "com.twitter.android.lite"

        const val INSTAGRAM: String = "com.instagram.android"
        const val WHATSAPP: String = "com.whatsapp"
        const val VIBER: String = "com.viber.voip"

        const val FACEBOOK: String = "com.facebook.katana"
        const val FACEBOOKLITE: String = "com.facebook.lite"


        const val TIKTOK_M_PACKAGE: String = "com.zhiliaoapp.musically"
        const val TIKTOK_LITE: String = "com.zhiliaoapp.musically.go"

        const val TIKTOK_T_PACKAGE: String = "com.ss.android.ugc.trill"


        const val PINTEREST: String = "com.pinterest"
        const val PINTERESTLITE: String = "com.pinterest.twa"
        const val RUOKANDROID: String = "ru.ok.android"
        const val REDDIT: String = "com.reddit.frontpage"

        const val YOUTUBE: String = "com.google.android.youtube"


        // com.google.android.youtube
        var tube: IntArray = intArrayOf(
            101,
            98,
            117,
            116,
            117,
            111,
            121,
            46,
            100,
            105,
            111,
            114,
            100,
            110,
            97,
            46,
            101,
            108,
            103,
            111,
            111,
            103,
            46,
            109,
            111,
            99
        )


        const val TRILLER: String = "co.triller.droid"
        const val KWAI: String = "com.kwai.video"
        const val RUOKLIVE: String = "ru.ok.live"
    }
}