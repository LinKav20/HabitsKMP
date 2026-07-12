package ru.template

import org.gradle.kotlin.dsl.invoke
import ru.template.utils.applyIfNeeded
import ru.template.utils.kmpConfig
import ru.template.utils.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.compose.multiplatform.get().pluginId)
plugins.applyIfNeeded("ru.template.kmp.library.all")
plugins.applyIfNeeded(libs.plugins.jetbrains.compose.compiler.get().pluginId)
plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.serialization.get().pluginId)
plugins.applyIfNeeded("ru.template.jetbrains-compose.all")

kmpConfig {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.components.resources)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.decompose.decompose)
            implementation(libs.decompose.extensionsComposeExpiremental)
            implementation(libs.decompose.extensionsCompose)

            implementation(libs.essenty.lifecycle)
            implementation(libs.essenty.lifecycle.coroutines)
            implementation(libs.essenty.stateKeeper)
            implementation(libs.essenty.instanceKeeper)
            implementation(libs.essenty.backHandler)

            implementation(libs.mvikotlin)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.logging)
            implementation(libs.mvikotlin.timetravel)
            implementation(libs.mvikotlin.extensions.coroutines)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(project.dependencies.platform(libs.koin.annotations.bom))
            implementation(libs.koin.annotations)
        }
    }
}
