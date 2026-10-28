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
            implementation(projects.features.profile.api)
            implementation(projects.core.utils)
            implementation(projects.features.debugmenu.api)
        }
    }
}

compose.resources {
    packageOfResClass = "com.github.linkav20.habits.profile.impl.resources"
    generateResClass = auto
}
