import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.impl)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.profile.impl"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.components.resources)
            implementation(projects.features.main.api)
            implementation(projects.core.utils)
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(libs.emoji.kt)
            implementation(libs.emoji.compose.m3)
        }
    }
}

compose.resources {
    packageOfResClass = "com.github.linkav20.habits.profile.impl.resources"
    generateResClass = auto
}
