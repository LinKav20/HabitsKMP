package ru.template

import org.gradle.kotlin.dsl.invoke
import ru.template.utils.applyIfNeeded
import ru.template.utils.composeExt
import ru.template.utils.kmpConfig
import ru.template.utils.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.compose.compiler.get().pluginId)
plugins.applyIfNeeded(libs.plugins.jetbrains.compose.multiplatform.get().pluginId)
plugins.applyIfNeeded(libs.plugins.android.kotlin.multiplatform.library.get().pluginId)

kmpConfig {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:uikit"))

            implementation(composeExt.dependencies.components.resources)
            implementation(composeExt.dependencies.components.uiToolingPreview)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
    }
}
