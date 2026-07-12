import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.datastore"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.datastore.preferences)

            implementation(projects.core.utils)
            implementation(libs.kotlinx.serialization.json)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            api(libs.datastore.preferences.android)
            implementation(libs.splitties.appcontext)
        }

        iosMain.dependencies {}
    }
}
