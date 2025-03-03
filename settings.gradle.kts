rootProject.name = "ui"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        //noinspection JcenterRepositoryObsolete
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

apply(from = "kwk\\corelib\\submodules.gradle")

include(":app")

include(":features:ui")
include(":features:wads")
include(":features:landing")
include(":features:webview")

include(":stub:stubInjector")
//include(":example:phonenumber")
include(":promo")
include(":wvcompose")

include(":threader")
project(":threader").projectDir = File("../multithreader/threader/")

include(":shared")
