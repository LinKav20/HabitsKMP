package ru.template

import ru.template.utils.applyIfNeeded
import ru.template.utils.config.IOSPlatform
import ru.template.utils.kmpConfig
import ru.template.utils.config.kmpIosPlatforms
import ru.template.utils.libs

plugins.applyIfNeeded(libs.plugins.jetbrains.kotlin.multiplatform.get().pluginId)
plugins.applyIfNeeded("ru.template.kmp.library.base")

kmpConfig {
    kmpIosPlatforms.asSequence()
        .map {
            when (it) {
                IOSPlatform.ARM_64 -> iosArm64()
                IOSPlatform.SIMULATOR_ARM64 -> iosSimulatorArm64()
                IOSPlatform.SIMULATOR_X64 -> iosX64()
            }
        }.forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = project.name
                isStatic = true
            }
        }
}
