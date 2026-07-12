@file:Suppress("unused")

package com.github.linkav20.habits.core.uikit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun HabitTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    // Use to create color theme https://material-foundation.github.io/material-theme-builder/
    colors: HabitColorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme(),
    shapes: Shapes = shapes(),
    typography: HabitTypography = templateTypography(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalColorScheme provides colors,
        LocalShapes provides shapes,
        LocalTypography provides typography,
        LocalIsDarkModeScheme provides isDarkMode,
    ) {
        MaterialTheme(
            colorScheme = colors.toM3ColorScheme(),
            shapes = shapes.toM3Shapes(),
            content = content,
        )
    }
}

object HabitTheme {

    val darkModeScheme: Boolean
        @[Composable ReadOnlyComposable] get() = LocalIsDarkModeScheme.current

    val colors: HabitColorScheme
        @[Composable ReadOnlyComposable] get() = LocalColorScheme.current

    val typography: HabitTypography
        @[Composable ReadOnlyComposable] get() = LocalTypography.current

    val shapes: Shapes
        @[Composable ReadOnlyComposable] get() = LocalShapes.current
}
