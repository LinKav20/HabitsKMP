plugins {
    alias(libs.plugins.main.kmplib.ios)
    alias(libs.plugins.main.compose.ios)
}

kotlin {
    androidLibrary {
        namespace = "com.github.linkav20.habits"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeApp)
            implementation(projects.core.network)
            // Необходимо явно добавить все зависимости для работы
            // приложения на iOS, чтобы они попали в XCFramework
            implementation(compose.runtime)
            implementation(compose.ui)

            implementation(libs.decompose.decompose)
            implementation(libs.decompose.extensionsCompose)
            implementation(libs.essenty.lifecycle)
            implementation(libs.essenty.stateKeeper)
            implementation(libs.essenty.backHandler)
        }
    }
    
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.configureEach {
            compilerOptions {
                freeCompilerArgs.add("-Xbinary=bundleId=com.github.linkav20.habits.ios")
            }
        }
    }
}
