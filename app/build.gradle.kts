import java.util.Properties
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "kgbook.ru"
    
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    val versionPropsFile = file("version.properties")

    if (versionPropsFile.canRead()) {
        val versionProps = Properties()
        versionProps.load(FileInputStream(versionPropsFile))
        
        val code = versionCodeDate()

        defaultConfig {
            resourceConfigurations.addAll(listOf("ru", "uk", "en"))

            multiDexEnabled = true
            vectorDrawables {
                useSupportLibrary = true
            }
            
            applicationId = "kgbook.ru"
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
            versionCode = code
            versionName = "1.1.$code.release"
            setProperty("archivesBaseName", "kgbook.ru")
            testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    signingConfigs {
        create("x") {
            keyAlias = "key0"
            keyPassword = "kgbookpass"
            storeFile = file("keystore/kwk_jakubai_kgbook.jks")
            storePassword = "kgbookpass"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("x")
        }
        debug {
            signingConfig = signingConfigs.getByName("x")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    
    implementation(project(":features:ui"))
    implementation(project(":promo"))
    
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.multidex)
}

fun versionCodeDate(): Int {
    return SimpleDateFormat("yyMMdd").format(Date()).toInt()
} 