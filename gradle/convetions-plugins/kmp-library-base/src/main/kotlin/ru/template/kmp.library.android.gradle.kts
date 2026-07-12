package ru.template

import org.gradle.kotlin.dsl.invoke
import ru.template.utils.androidLibrary
import ru.template.utils.applyIfNeeded
import ru.template.utils.kmpConfig
import ru.template.utils.libs

plugins.applyIfNeeded("ru.template.kmp.library.base")

kmpConfig {
    androidLibrary {
        androidResources {
            enable = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.kotlinx.coroutines.android)
        }

        val androidHostTest = findByName("androidHostTest")
        androidHostTest?.dependencies {
            implementation(libs.kotlin.test.junit)
            implementation(libs.junit)
        }
    }
}
