package com.github.linkav20.habits.core.uikit.components.charts.grid.axisscale

class FixedTicksXAxisScale(
    override val min: Float,
    override val max: Float,
    tickCount: Int
) : XAxisScale {
    override val tick: Float = (max - min) / tickCount
    override val start: Float = min
}
