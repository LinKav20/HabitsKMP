import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.api)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.auth.api"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.mvi.navigation)
        }
    }
}
