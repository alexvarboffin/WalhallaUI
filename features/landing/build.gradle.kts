plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            // minifyEnabled false not_use in lib
        }
        getByName("release") {
            // minifyEnabled true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    namespace = "com.walhalla.landing"

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    api(libs.androidx.preference)

    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.jsoup)

    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra["okHttpVersion"]}")
    implementation(libs.gson)
    implementation(project(":features:ui"))

    implementation(libs.oopsnointernet)
    implementation(libs.taptargetview)
    api(libs.lottie)
    api(project(":features:webview"))

    // Web Libs
    api(libs.androidx.browser)
    implementation(libs.androidbrowserhelper)
    implementation(libs.androidx.core.ktx)

    // Features
    implementation(libs.androidx.core.splashscreen)
}