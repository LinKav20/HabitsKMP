package ru.template

import org.gradle.kotlin.dsl.invoke
import ru.template.utils.android
import ru.template.utils.applyIfNeeded
import ru.template.utils.kmpConfig
import ru.template.utils.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)
plugins.applyIfNeeded(libs.plugins.android.kotlin.multiplatform.library.get().pluginId)

kmpConfig {
    android {
        compileSdk { version = release(libs.versions.android.compileSdk.get().toInt()) }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.annotation)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
