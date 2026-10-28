import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.impl)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.debugmenu.impl"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.components.resources)
            implementation(projects.features.debugmenu.api)
            implementation(projects.core.utils)
            implementation(projects.core.network)
            implementation(projects.core.datastore)
            implementation(projects.core.featureflags)
        }
    }
}

compose.resources {
    packageOfResClass = "com.github.linkav20.habits.debugmenu.impl.resources"
    generateResClass = auto
}
