import org.gradle.api.JavaVersion

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}

var ONESIGNAL_APP_ID = ""//$rootProject.ONESIGNAL_APP_ID
var APPSFLYER_DEV_KEY = ""//$rootProject.APPSFLYER_DEV_KEY


android {
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    namespace = "org.apache.cordova"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("debug") {
            // minifyEnabled false not used in library
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

fun isEmpty(a: String?): Boolean {
    return a == null || a.isEmpty()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:$rootProject.compatVersion")
    implementation("com.google.android.material:material:$rootProject.materialVersion")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference:1.2.1")

    //    testImplementation "junit:junit:4.12"
    //    androidTestImplementation "com.android.support.test:runner:1.0.2"
    //    androidTestImplementation "com.android.support.test.espresso:espresso-core:3.0.2"

    //snakbar
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    //implementation "com.facebook.android:facebook-android-sdk:7.1.0"

    //4.8.2
    //implementation "com.onesignal:OneSignal:[4.0.0, 4.99.99]"
    if (isEmpty(ONESIGNAL_APP_ID)) {
        implementation(project (":stub:stub_onesignal"))
    } else {
        implementation("com.onesignal:OneSignal:$rootProject.OneSignal")
    }

    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")
    //Firebase Crashlytics
    //@@implementation "com.google.firebase:firebase-crashlytics:$rootProject.crashlyticsVersion"
    //@@implementation "com.google.firebase:firebase-analytics:$rootProject.analyticsVersion"

    //Нужно для захвата рекламного идентификатора
    //@@@ требует плагина и прописать в манифест!!! implementation "com.google.android.gms:play-services-ads-lite:22.6.0"
    //Не требует плагина и прописать в манифест!!!
    implementation ("com.google.android.gms:play-services-ads-identifier:18.2.0")
    implementation ("androidx.ads:ads-identifier:1.0.0-alpha05")
    //yandex
    // AppMetrica SDK.
    //implementation "com.yandex.android:mobmetricalib:3.18.0"

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    api (project (":features:webview"))
    // define a BOM and its version
    //implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))

    // define any required OkHttp artifacts without version
    implementation ("com.squareup.okhttp3:okhttp:$rootProject.okHttpVersion")
    implementation ("com.squareup.okhttp3:logging-interceptor:$rootProject.okHttpVersion")


    // Used for the calls to addCallback() in the snippets on this page.
    implementation ("com.google.guava:guava:31.1-jre")
    implementation ("androidx.annotation:annotation:1.7.1")

    //implementation "com.appsflyer:af-android-sdk:6.9.2@aar"
    if (isEmpty(APPSFLYER_DEV_KEY)) {
        implementation (project (":stub:stub_appsflyer"))
    } else {
        implementation ("com.appsflyer:af-android-sdk:6.9.2")
    }

    //Web Libs
    //TwaLauncher or CustomTabsIntent
    //Chrome Custom Tabs -> deprecated
    //Custom Tabs vs Trusted Web Activity (Trusted Web Activity extends Custom Tabs)
    implementation ("androidx.browser:browser:1.8.0") //It"s android custom tabs lib
    implementation ("com.google.androidbrowserhelper:androidbrowserhelper:2.5.0")

    // TWA 2.3.0 contains androidx.browser:browser:1.4.0
    api (project (":features:ui"))
    api (project (":kwk:StelthCore"))
    //implementation "com.github.acsbendi:Android-Request-Inspector-WebView:1.0.2"
    implementation ("org.imaginativeworld.oopsnointernet:oopsnointernet:2.0.0")
    implementation ("androidx.core:core-ktx:1.15.0")
    //implementation project(":features:webview")
}