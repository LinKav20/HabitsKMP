plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.jetbrains.compose.multiplatform) apply false
    alias(libs.plugins.jetbrains.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ktorfit) apply false
    alias(libs.plugins.manifestGuard) apply false
    alias(libs.plugins.main.detekt) apply true
    alias(libs.plugins.main.ktlint) apply true
    alias(libs.plugins.room) apply false
    alias(libs.plugins.google.services) apply false
}

tasks.register<Copy>("installGitHooks") {
    val gitHooksFileNames = listOf(
        "pre-push",
    )

    group = "build setup"
    description = "Installs Git hooks from scripts/githooks to .git/hooks/"
    val hooksFilesToCopy = rootProject.files(gitHooksFileNames.map { fileName -> file("scripts/githooks/$fileName") })
    from(hooksFilesToCopy)
    val gitHooksDir = rootProject.file(".git/hooks")
    into(gitHooksDir)

    doLast {
        rootProject.files(gitHooksFileNames.map { fileName -> File(gitHooksDir, fileName) })
            .forEach { hookFile -> hookFile.setExecutable(true) }
        println("✅ Git hook installed successfully!")
    }
}
