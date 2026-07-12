@file:Suppress("UnstableApiUsage")

pluginManagement {
    val localProperties = java.util.Properties()
    if (file("local.properties").exists()) {
        localProperties.load(file("local.properties").inputStream())
    }

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    val localProperties = java.util.Properties()
    if (file("local.properties").exists()) {
        localProperties.load(file("local.properties").inputStream())
    }

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

include(":kmp-library-base")
