plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.main.compose.jetpack.base)
    alias(libs.plugins.manifestGuard)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.github.linkav20.habits"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.linkav20.habits"
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        versionCode = 14
        versionName = "0.0.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                "proguard-rules.pro",
            )
        }
    }

    lint {
        checkReleaseBuilds = false
        checkDependencies = true
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/native-image/io.sentry/sentry/native-image.properties",
                "/META-INF/{AL2.0,LGPL2.1}",
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters += listOf("en", "ru")
        generateLocaleConfig = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
    implementation(projects.composeApp)
    implementation(projects.core.utils)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.decompose.decompose)

    implementation(project.dependencies.platform(libs.okhttp.bom))
    implementation(libs.okhttp)

    implementation(libs.filekit.core)
    implementation(libs.filekit.dialogs)

    implementation(libs.pdf.viewer)
    implementation(libs.androidx.core)
}

androidComponents.beforeVariants { variant ->
    manifestGuard {
        guardVariant(variant.name) {
            enabled = true
            compareOnAssemble = false

            val baseDir = layout.projectDirectory.dir("manifest/${variant.name}")
            referenceFile = baseDir.file("manifest-original.xml")
            htmlDiffFile = baseDir.file("manifest-diff.html")

            ignore {
                ignoreAppVersionChanges = true
            }
        }
    }
}
