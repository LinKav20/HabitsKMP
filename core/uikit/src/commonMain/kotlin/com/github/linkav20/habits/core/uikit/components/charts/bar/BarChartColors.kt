package com.github.linkav20.habits.core.uikit.components.charts.bar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.github.linkav20.habits.core.uikit.components.charts.theme.ChartColors

@Immutable
data class BarChartColors(
    val grid: Color,
    val surface: Color,
)

val ChartColors.barChartColors
    get() = BarChartColors(
        grid = grid,
        surface = surface,
    )
