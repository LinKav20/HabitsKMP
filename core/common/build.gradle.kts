import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.core.common"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mvikotlin)
            implementation(libs.mvikotlin.extensions.coroutines)
            implementation(libs.kotlinx.serialization.json)
        }
    }

    explicitApi()
}
