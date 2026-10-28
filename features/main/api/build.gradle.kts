import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.api)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.main.api"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.mvi.navigation)
            implementation(projects.features.createitem.api)
            implementation(projects.features.habit.api)
            implementation(projects.features.todo.api)
            implementation(projects.features.profile.api)
            implementation(projects.core.utils)
        }
    }
}
