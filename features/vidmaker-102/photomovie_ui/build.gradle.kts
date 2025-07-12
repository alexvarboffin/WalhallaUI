plugins {
    alias(libs.plugins.android.library)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.video.maker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":features:wads"))
    implementation(libs.androidx.preference.ktx)
    //implementation project(':features:ui')
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)


    implementation(libs.advrecyclerview)
    implementation(libs.play.services.ads)
    implementation(libs.lottie)
    implementation(libs.circleimageview)

    implementation("com.intuit.sdp:sdp-android:1.1.1")
    //not this lib
    //implementation("com.burhanrashid52:photoeditor:3.0.2")
    //implementation("com.burhanrashid52:photoeditor:1.5.1")
    //implementation("com.burhanrashid52:photoeditor:3.0.2")
    implementation(libs.roundedimageview)
    implementation(project(":photomovie"))
    //implementation("com.github.yellowcath:PhotoMovie:1.6.4")

    //implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}