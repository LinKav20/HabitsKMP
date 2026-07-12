import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.jetbrains.compose.multiplatform)
    alias(libs.plugins.jetbrains.compose.compiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.ksp)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.core.uikit"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.essenty.backHandler)
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.components.uiToolingPreview)
            api(compose.material3AdaptiveNavigationSuite)
            api(compose.ui)
            api(libs.components.resources)
            api(libs.coil)
            api(libs.coil.svg)
            api(libs.coil.network)
            api(libs.kotlinx.collections.immutable)
            api(projects.core.common)
            api(libs.decompose.decompose)
            api(libs.decompose.extensionsCompose)
            api(libs.decompose.extensionsComposeExpiremental)
            implementation(libs.compose.webview.multiplatform)
            implementation(libs.emoji.kt)
            implementation(libs.emoji.compose.m3)
        }
    }
}

compose.resources {
    packageOfResClass = "com.github.linkav20.habits.core.uikit.resources"
    generateResClass = auto
}
