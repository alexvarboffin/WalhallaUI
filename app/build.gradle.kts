import com.android.build.gradle.internal.dsl.SigningConfig
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("kotlin-parcelize")

////    id("org.jetbrains.kotlin.android")
////    id("kotlin-android")
////    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
////    id("com.google.devtools.ksp")
//    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}
android {
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileSdk = 35
    buildToolsVersion = "35.0.0"

    namespace = "com.kworkapp.audiogid"

    val code = versionCodeDate()

    defaultConfig {
        resConfigs("sw", "en", "ru")
        vectorDrawables.useSupportLibrary = true
        applicationId = "com.kworkapp.audiogid"

        minSdk = 21
        targetSdk = 35
        versionCode = code
        versionName = "1.2.$code"
        setProperty("archivesBaseName", "audiogid-$code")
    }

    signingConfigs {
        create("d") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/keystore.jks")
            storePassword = "release"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("d")
            versionNameSuffix = "-DEMO"
        }
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("d")
            versionNameSuffix = ".release"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
//    com.appsflyer
//    com\bumptech\glide
//    implementation("com.airbnb.android:lottie:6.1.0")
//    implementation("com.github.Piashsarker:AndroidAppUpdateLibrary:1.0.4")
//    implementation("com.robinhood.ticker:ticker:2.0.4")
//    implementation("com.github.skydoves:balloon:1.6.3")

    //noinspection GradleDependency
    implementation(libs.exoplayer)
    implementation(libs.androidx.multidex)
    implementation(libs.gson)
    //implementation("com.github.chrisbanes:PhotoView:2.3.0")

    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    implementation(libs.google.firebase.crashlytics)
//    implementation("com.github.MoeidHeidari:banner:1.04")


    //implementation("com.jsibbold:zoomage:1.3.1")
    //implementation("com.bogdwellers:pinchtozoom:0.1")
    //implementation("com.github.MikeOrtiz:TouchImageView:1.4.1")
    //implementation("com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0")

    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    //implementation("com.github.chrisbanes:photoview:2.3.0")

    //implementation("com.chibde:audiovisualizer:2.2.0")
    implementation("io.github.gautamchibde:audiovisualizer:2.2.5")
    implementation(libs.androidx.preference.ktx)
//    implementation("com.github.duanhong169:colorpicker:1.1.6")
//    implementation("com.github.duanhong169:TextButton:1.0.5")
    implementation(project(":features:ui"))
}
fun versionCodeDate(): Int {
    return SimpleDateFormat("yyMMdd").format(Date()).toInt()
}