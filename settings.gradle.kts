rootProject.name = "Habits"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    includeBuild("gradle/convetions-plugins")

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
        mavenCentral()
        maven("https://jitpack.io")
    }
}

val coreLibsDir = "core"
file(coreLibsDir)
    .listFiles { file -> file.isDirectory }
    ?.forEach { file -> include(":${coreLibsDir}:${file.name}") }

include(":core:mvi:navigation")

include(":composeApp")
include(":main-android")
include(":main-ios")
project(":main-ios").name = "HabitsShared"

include(":features:debugmenu:api")
include(":features:debugmenu:impl")
include(":features:auth:api")
include(":features:auth:impl")
include(":features:main:api")
include(":features:main:impl")
include(":features:profile:api")
include(":features:profile:impl")
include(":features:createitem:api")
include(":features:createitem:impl")
include(":features:habit:api")
include(":features:habit:impl")
include(":features:todo:api")
include(":features:todo:impl")
