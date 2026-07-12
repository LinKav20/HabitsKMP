package com.github.linkav20.habits.core.uikit.components.charts.line

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import com.github.linkav20.habits.core.uikit.components.charts.ChartAnimation
import com.github.linkav20.habits.core.uikit.components.charts.startAnimation
import com.github.linkav20.habits.core.uikit.components.charts.grid.GridDefaults
import com.github.linkav20.habits.core.uikit.components.charts.grid.LineParameters
import com.github.linkav20.habits.core.uikit.components.charts.grid.alignCenterToOffsetHorizontal
import com.github.linkav20.habits.core.uikit.components.charts.grid.axisscale.FixedTicksXAxisScale
import com.github.linkav20.habits.core.uikit.components.charts.grid.axisscale.YAxisScale
import com.github.linkav20.habits.core.uikit.components.charts.grid.measureChartGrid

val dashedPathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LineChart(
    lineChartData: LineChartData,
    modifier: Modifier = Modifier,
    xAxisLabel: @Composable (value: Any) -> Unit = {},
    yAxisLabel: @Composable (value: Any) -> Unit = { },
    animation: ChartAnimation = ChartAnimation.Simple(),
    maxVerticalLines: Int = GridDefaults.NUMBER_OF_GRID_VERTICAL_LINES,
    maxHorizontalLines: Int = GridDefaults.NUMBER_OF_GRID_HORIZONTAL_LINES,
    minVerticalLines: Int = GridDefaults.MIN_NUMBER_OF_GRID_VERTICAL_LINES,
    minHorizontalLines: Int = GridDefaults.MIN_NUMBER_OF_GRID_HORIZONTAL_LINES,
    roundMinMaxClosestTo: Int = GridDefaults.ROUND_MIN_MAX_CLOSEST_TO,
) {
    var verticalGridLines by remember { mutableStateOf(emptyList<LineParameters>()) }
    var horizontalGridLines by remember { mutableStateOf(emptyList<LineParameters>()) }
    val horizontalLinesOffset: Dp = GridDefaults.HORIZONTAL_LINES_OFFSET

    val animationPlayed = startAnimation(animation, lineChartData)

    val alpha = when (animation) {
        ChartAnimation.Disabled -> lineChartData.series.indices.map { 1f }
        is ChartAnimation.Simple -> lineChartData.series.indices.map {
            animateFloatAsState(
                targetValue = if (animationPlayed) 1f else 0f,
                animationSpec = animation.animationSpec(),
            ).value
        }

        is ChartAnimation.Sequenced -> lineChartData.series.indices.map {
            animateFloatAsState(
                targetValue = if (animationPlayed) 1f else 0f,
                animationSpec = animation.animationSpec(it),
            ).value
        }
    }

    Row(modifier = modifier) {
//        YAxisLabels(
//            horizontalGridLines = horizontalGridLines,
//            yAxisMarkerLayout = yAxisLabel,
//        )
//
//        Spacer(modifier = Modifier.size(4.dp, 0.dp))

        Column(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .drawBehind {
                        val lines = measureChartGrid(
                            xAxisScale = FixedTicksXAxisScale(
                                min = minVerticalLines.toFloat(),
                                max = maxVerticalLines.toFloat(),
                                tickCount = 7,
                            ),
                            yAxisScale = YAxisScale(
                                min = minHorizontalLines.toFloat(),
                                max = maxHorizontalLines.toFloat(),
                                maxTickCount = maxHorizontalLines - 1,
                                roundClosestTo = 1,
                            ),
                            horizontalLinesOffset = horizontalLinesOffset,
                        )
                        verticalGridLines = lines.verticalLines
                        horizontalGridLines = lines.horizontalLines
                        drawLineChart(
                            lineChartData = lineChartData,
                            graphTopPadding = horizontalLinesOffset,
                            graphBottomPadding = horizontalLinesOffset,
                            alpha = alpha,
                            lines = lines,
                        )
                    },
            )

            Box(Modifier.fillMaxWidth()) {
                for (gridLine in verticalGridLines) {
                    Box(
                        modifier = Modifier
                            .alignCenterToOffsetHorizontal(gridLine.position),
                    ) {
                        xAxisLabel(gridLine.value.toLong())
                    }
                }
            }
        }
    }
}
