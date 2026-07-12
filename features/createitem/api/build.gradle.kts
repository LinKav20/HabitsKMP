import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.api)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.createitem.api"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.mvi.navigation)
            implementation(projects.core.utils)
        }
    }
}
