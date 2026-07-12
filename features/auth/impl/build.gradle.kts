import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.impl)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.auth.impl"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(projects.core.utils)
            implementation(projects.features.auth.api)
        }
    }
}

compose.resources {
    packageOfResClass = "com.github.linkav20.habits.auth.impl.resources"
    generateResClass = auto
}
