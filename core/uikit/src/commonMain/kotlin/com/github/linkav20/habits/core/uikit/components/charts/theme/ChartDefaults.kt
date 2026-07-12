package com.github.linkav20.habits.core.uikit.components.charts.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.linkav20.habits.core.uikit.HabitTheme

/**
 * Contains the default values used by all charts
 */
object ChartDefaults {
    @Composable
    fun chartColors(
        primary: Color = HabitTheme.colors.primary,
        surface: Color = Color.Unspecified,
        grid: Color = HabitTheme.colors.onSurface.copy(alpha = 0.4f),
        emptyGasBottle: Color = HabitTheme.colors.error,
        fullGasBottle: Color = HabitTheme.colors.primary,
        overlayLine: Color = HabitTheme.colors.error,
    ) = ChartColors(
        primary = primary,
        surface = surface,
        grid = grid,
        emptyGasBottle = emptyGasBottle,
        fullGasBottle = fullGasBottle,
        overlayLine = overlayLine,
    )
}
