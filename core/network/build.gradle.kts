import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.ktorfit)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.network"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.datastore)
            implementation(projects.core.utils)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktorfit)
            implementation(libs.ktor.client.logging)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(libs.filekit.core)

            api(projects.core.common)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.engine.okhttp)
            implementation(libs.splitties.appcontext)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.engine.darwin)
        }
    }
    ktorfit {
        compilerPluginVersion.set(libs.versions.ktorfit.compiler)
    }
}
