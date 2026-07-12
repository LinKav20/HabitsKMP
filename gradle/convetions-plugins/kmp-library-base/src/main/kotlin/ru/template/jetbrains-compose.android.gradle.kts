package ru.template

import ru.template.utils.applyIfNeeded
import ru.template.utils.composeExt
import ru.template.utils.kmpConfig
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke

plugins.applyIfNeeded("ru.template.jetbrains-compose.base")
plugins.apply("ru.template.jetpack-compose.base")

kmpConfig {
    sourceSets {
        androidMain.dependencies {
            implementation(composeExt.dependencies.preview)
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(composeExt.dependencies.uiTooling)
}
