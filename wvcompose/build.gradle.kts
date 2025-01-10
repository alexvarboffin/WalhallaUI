extra.apply {
    set("minSdkVersion", 21)
    set("okHttpVersion", "4.12.0")
    set("ONESIGNAL_APP_ID", "")
    set("APPSFLYER_DEV_KEY", "")
    set("FACEBOOK_SDK", "")
    set("MYTRACKER_KEY", "")
    set("APP_METRICA", "")
}

plugins {
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

fun versionCodeDate(): Int {
    return java.text.SimpleDateFormat("yyMMdd").format(java.util.Date()).toInt()
} 