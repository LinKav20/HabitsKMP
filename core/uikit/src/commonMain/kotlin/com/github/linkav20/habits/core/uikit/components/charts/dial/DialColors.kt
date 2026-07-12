package com.github.linkav20.habits.core.uikit.components.charts.dial

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.github.linkav20.habits.core.uikit.components.charts.theme.ChartColors

@Immutable
data class DialColors(
    val progressBarColor: Color,
    val progressBarBackgroundColor: Color,
    val gridScaleColor: Color,
)

val ChartColors.dialColors
    get() = DialColors(
        progressBarColor = primary,
        progressBarBackgroundColor = grid,
        gridScaleColor = grid
    )
