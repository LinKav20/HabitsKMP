package com.github.linkav20.habits.core.uikit.statusbar

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

public actual val statusBar: StatusBar = Android

public object Android : StatusBar {
    @Composable
    override fun SetStatusBarLightMode(isDarkStatusBarColor: Boolean) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            LaunchedEffect(isDarkStatusBarColor) {
                val window = (view.context as Activity).window
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = isDarkStatusBarColor
                }
            }
        }
    }
}
