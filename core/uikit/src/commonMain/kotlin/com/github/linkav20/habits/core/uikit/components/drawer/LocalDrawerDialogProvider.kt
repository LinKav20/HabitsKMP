package com.github.linkav20.habits.core.uikit.components.drawer

import androidx.compose.runtime.compositionLocalOf

val LocalDrawerDialogProvider = compositionLocalOf<DrawerDialog> {
    error("No DrawerDialog provided")
}
