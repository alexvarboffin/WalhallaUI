plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

fun isEmpty(a: String?): Boolean {
    return a == null || a.isEmpty()
}

android {
    namespace = "com.walhalla.stubinjector"
    compileSdk = 35

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":features:ui"))

    if (isEmpty(rootProject.extra["APPSFLYER_DEV_KEY"] as String?)) {
        implementation(project(":stub:stub_appsflyer"))
    } else {
        implementation("com.appsflyer:af-android-sdk:6.9.2")
    }
    
    if (isEmpty(rootProject.extra["FACEBOOK_SDK"] as String?)) {
        implementation(project(":stub:stub_facebook"))
    } else {
        implementation("com.facebook.android:facebook-android-sdk:14.1.0")
    }
    
    if (isEmpty(rootProject.extra["MYTRACKER_KEY"] as String?)) {
        implementation(project(":stub:stub_mtracker"))
    } else {
        implementation("com.my.tracker:mytracker-sdk:3.0.+")
    }
    
    if (isEmpty(rootProject.extra["APP_METRICA"] as String?)) {
        implementation(project(":stub:stub_appmetrica"))
    } else {
        implementation("com.yandex.android:mobmetricalib:3.10.2")
    }
    
    if (isEmpty(rootProject.extra["ONESIGNAL_APP_ID"] as String?)) {
        implementation(project(":stub:stub_onesignal"))
    } else {
        implementation("com.onesignal:OneSignal:${rootProject.extra["OneSignal"]}")
    }
}