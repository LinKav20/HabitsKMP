import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.feature.impl)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.core.mvi.navigation"
    }
}
