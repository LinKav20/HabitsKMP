import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.core.featureflags"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
    }
}
