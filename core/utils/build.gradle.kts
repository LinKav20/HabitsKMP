import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.jetbrains.compose.multiplatform)
    alias(libs.plugins.jetbrains.compose.compiler)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.core.utils"
    }
    sourceSets {
        commonMain.dependencies {
            api(compose.foundation)
            implementation(libs.components.resources)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(project.dependencies.platform(libs.koin.annotations.bom))
            implementation(libs.koin.annotations)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.splitties.appcontext)
            implementation(libs.androidx.lifecycle.process)
            implementation(libs.koin.android)
        }
    }

    explicitApi()
}

compose.resources {
    packageOfResClass = "com.github.linkav20.habits.core.utils.resources"
    generateResClass = auto
}
