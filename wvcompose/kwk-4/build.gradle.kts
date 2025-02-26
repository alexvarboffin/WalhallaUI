plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}android {
    namespace = "com.walhalla.wvcompose4"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.walhalla.wvcompose4"
        minSdk = 24
        targetSdk = 35
        versionCode = 4001
        versionName = "4.0.0.1"

        setProperty("archivesBaseName", "wvcompose4-webview-slotmachine-material3-compose-${versionName}")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("config") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("../keystore/keystore.jks")
            storePassword = "release"
        }
    }
    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("config")
        }

        getByName("release") {
            versionNameSuffix = ".release"
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}