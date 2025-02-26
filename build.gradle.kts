extra.apply {
    set("minSdkVersion", 21)
    set("okHttpVersion", "4.12.0")
    set("ONESIGNAL_APP_ID", "")
    set("APPSFLYER_DEV_KEY", "")
    set("FACEBOOK_SDK", "")
    set("MYTRACKER_KEY", "")
    set("APP_METRICA", "")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}
//plugins {
//    alias(libs.plugins.kotlinAndroid) apply false
//
//}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        mavenLocal()
//        jcenter()
//        maven { url = uri("https://jitpack.io") }
//    }
//}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

fun versionCodeDate(): Int {
    return java.text.SimpleDateFormat("yyMMdd").format(java.util.Date()).toInt()
} 