@file:Suppress("ktlint")

package ru.template.utils

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

fun KotlinMultiplatformExtension.`androidLibrary`(
    configure: Action<com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget>
): Unit = (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("androidLibrary", configure)

fun KotlinMultiplatformExtension.`android`(configure: Action<com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("android", configure)

internal fun Project.kotlinJvmCompilerOptions(block: KotlinJvmCompilerOptions.() -> Unit) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions(block)
    }
}

internal val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

private val Project.kmpExtension: KotlinMultiplatformExtension
    get() {
        return extensions.findByType(KotlinMultiplatformExtension::class) ?: error(
            "\"Project.kmpExtension\" value may be called only " + "from kotlin multiplatform gradle script",
        )
    }

internal inline fun Project.kmpConfig(
    block: KotlinMultiplatformExtension.() -> Unit
) = kmpExtension.block()

internal val Project.composeExt: ComposeExtension
    get() = extensions.findByType(ComposeExtension::class.java)
        ?: error("Compose plugin is not applied")

internal val Project.detektExtension: DetektExtension
    get() = checkNotNull(extensions.findByType(DetektExtension::class))

internal fun Project.detektConfig(block: DetektExtension.() -> Unit) = block(detektExtension)
