package com.github.linkav20.habits.core.uikit.components.charts.bar

import androidx.compose.runtime.Immutable
import com.github.linkav20.habits.core.uikit.components.charts.grid.GridChartData
import com.github.linkav20.habits.core.uikit.components.charts.line.LegendItemData
import com.github.linkav20.habits.core.uikit.components.charts.line.SymbolShape

@Immutable
data class BarChartData(
    val categories: List<BarChartCategory>,
) : GridChartData {
    // TODO hide those values from the user
    override val minX: Float = 0f
    override val maxX: Float
        get() = if (categories.isEmpty()) {
            1f
        } else {
            categories.size * 2.toFloat()
        }

    override val minY: Float
        get() = if (categories.isEmpty()) {
            0f
        } else {
            categories.minOf { it.minY }.coerceAtMost(0f)
        }

    override val maxY: Float
        get() = if (categories.isEmpty()) {
            1f
        } else {
            categories.maxOf { it.maxY }.coerceAtLeast(0f)
        }

    override val legendData: List<LegendItemData>
        get() {
            return categories
                .flatMap { it.entries }
                .distinctBy { it.x }
                .map {
                    LegendItemData(
                        name = it.x,
                        symbolShape = SymbolShape.RECTANGLE,
                        color = it.color,
                        dashed = false,
                    )
                }
        }
}
