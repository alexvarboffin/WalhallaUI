plugins {
    id("com.android.library")
    id("kotlin-android")
    // id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.yandex.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(
                file("D:\\walhalla\\sdk\\android\\ui\\stub\\proguard\\consumer-rules_empty_stub.pro")
        )
    }

    buildTypes {
        getByName("debug") {
            // isMinifyEnabled = false // not_use in lib
        }
        getByName("release") {
            //isMinifyEnabled = true
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

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    // androidTestImplementation(libs.androidx.test.junit)
    // androidTestImplementation(libs.androidx.espresso.core)
}