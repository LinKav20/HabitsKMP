package com.github.linkav20.habits.core.uikit.components.charts.grid

import com.github.linkav20.habits.core.uikit.components.charts.line.LegendItemData

interface GridChartData {
    val minX: Float
    val maxX: Float
    val minY: Float
    val maxY: Float
    val legendData: List<LegendItemData>
}
