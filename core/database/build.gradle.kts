import ru.template.utils.androidLibrary

plugins {
    alias(libs.plugins.main.kmplib)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.google.ksp)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits.database"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.utils)
            implementation(libs.kotlinx.serialization.json)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }

        androidMain.dependencies {
            implementation(libs.splitties.appcontext)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}
