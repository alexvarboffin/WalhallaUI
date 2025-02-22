import com.android.build.gradle.internal.dsl.SigningConfig
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.android.library) apply false
    //id("kotlin-parcelize") apply false
}


extra.apply {
    set("minSdkVersion", 21)
    set("kotlin_version", "1.9.22")
    set("okHttpVersion", "3.12.12")
    set("APP_METRICA", "")
    set("MYTRACKER_KEY", "")
    set("FACEBOOK_SDK", "")
    set("ONESIGNAL_APP_ID", "")
    set("APPSFLYER_DEV_KEY", "")
}

//buildscript {
//    extra.apply {
//        set("kotlin_version", "1.9.22")
//    }
//
//    repositories {
//        google()
//        mavenCentral()
//        jcenter()
//        maven { url = uri("https://jitpack.io") }
//    }
//
//    dependencies {
//        classpath("com.android.tools.build:gradle:8.4.1")
//        classpath("com.google.gms:google-services:4.4.2")
//        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.1")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
//    }
//}

fun versionCodeDate(): Int {
    return SimpleDateFormat("yyMMdd").format(Date()).toInt()
}

//fun isEmpty(a: String): Boolean {
//    return a.isNullOrEmpty()
//}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}