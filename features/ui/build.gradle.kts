buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
    }
}

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = 35
    buildToolsVersion = rootProject.extra["buildToolsVersion0"].toString()

    namespace = "com.walhalla.ui"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            //isMinifyEnabled = false // not used in lib
        }
        release {
            //isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)

    api(libs.androidx.preference)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.jsoup)

    // Google Play In-App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // Google Play In-App Review
    implementation(libs.review)
    implementation(libs.review.ktx)
} 