import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.main.feature.impl)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.compose"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(projects.core.featureflags)
            implementation(projects.core.database)
            implementation(projects.core.network)
            implementation(projects.core.utils)

            implementation(projects.features.debugmenu.api)
            implementation(projects.features.debugmenu.impl)

            implementation(projects.features.auth.api)
            implementation(projects.features.auth.impl)

            implementation(projects.features.main.api)
            implementation(projects.features.main.impl)

            implementation(projects.features.profile.api)
            implementation(projects.features.profile.impl)

            implementation(projects.features.createitem.api)
            implementation(projects.features.createitem.impl)

            implementation(projects.features.habit.api)
            implementation(projects.features.habit.impl)

            implementation(projects.features.todo.api)
            implementation(projects.features.todo.impl)
        }

        androidMain.dependencies {
            implementation(libs.splitties.appcontext)
        }
    }
}
