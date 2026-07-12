package com.github.linkav20.habits.core.uikit.statusbar

import androidx.compose.runtime.Composable
import com.github.linkav20.habits.core.uikit.HabitTheme

@Composable
fun SetStatusBarLightModeThemable(isDarkStatusBarColor: Boolean) {
    val setDarkStatus = if (HabitTheme.darkModeScheme) {
        !isDarkStatusBarColor
    } else {
        isDarkStatusBarColor
    }

    statusBar.SetStatusBarLightMode(setDarkStatus)
}

public interface StatusBar {
    @Composable
    public fun SetStatusBarLightMode(isDarkStatusBarColor: Boolean)
}

public expect val statusBar: StatusBar
