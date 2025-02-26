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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "example"
include(":app")
include(":kwk-1")
include(":kwk-2")
include(":kwk-3")
include(":kwk-4")
include(":kwk-5")
include(":kwk-6")
include(":kwk-7")
include(":kwk-8")

 