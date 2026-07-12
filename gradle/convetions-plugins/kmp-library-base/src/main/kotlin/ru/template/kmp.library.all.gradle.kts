package ru.template

import ru.template.utils.applyIfNeeded
import ru.template.utils.kotlinJvmCompilerOptions

plugins.applyIfNeeded("ru.template.kmp.library.android")
plugins.applyIfNeeded("ru.template.kmp.library.ios")

kotlinJvmCompilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
}
