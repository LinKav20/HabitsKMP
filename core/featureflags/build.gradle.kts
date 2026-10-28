import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.core.featureflags"
    }
}
