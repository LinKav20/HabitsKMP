import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.api)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.profile.api"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.mvi.navigation)
            api(projects.features.habit.api)
            implementation(projects.core.utils)
            api(projects.features.debugmenu.api)
        }
    }
}
