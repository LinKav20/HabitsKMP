package ru.template

import org.gradle.kotlin.dsl.invoke
import ru.template.utils.applyIfNeeded
import ru.template.utils.kmpConfig
import ru.template.utils.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.compose.multiplatform.get().pluginId)
plugins.applyIfNeeded("ru.template.kmp.library.all")
plugins.applyIfNeeded(libs.plugins.jetbrains.compose.compiler.get().pluginId)
plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.serialization.get().pluginId)
plugins.applyIfNeeded("ru.template.jetbrains-compose.base")

kmpConfig {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.mvikotlin)

            implementation(libs.decompose.decompose)
            implementation(libs.decompose.extensionsCompose)
            implementation(libs.decompose.extensionsComposeExpiremental)
        }
    }
}
