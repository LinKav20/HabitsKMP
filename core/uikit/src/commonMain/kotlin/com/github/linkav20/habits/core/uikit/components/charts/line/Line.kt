package com.github.linkav20.habits.core.uikit.components.charts.line

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.components.charts.grid.ChartGrid
import com.github.linkav20.habits.core.uikit.components.charts.mapValueToDifferentRange

internal fun DrawScope.drawLineChart(
    lineChartData: LineChartData,
    graphTopPadding: Dp,
    graphBottomPadding: Dp,
    alpha: List<Float>,
    lines: ChartGrid? = null,
) {
    val path = Path()
    val globalMinY = lines?.horizontalLines?.minOfOrNull { it.value.toFloat() } ?: lineChartData.minY
    val globalMaxY = lines?.horizontalLines?.maxOfOrNull { it.value.toFloat() } ?: lineChartData.maxY
    val globalMinX = lines?.verticalLines?.minOfOrNull { it.value.toFloat() } ?: lineChartData.minX
    val globalMaxX = lines?.verticalLines?.maxOfOrNull { it.value.toFloat() } ?: lineChartData.maxX
    lineChartData.series.forEachIndexed { seriesIndex, data ->
        val mappedPoints =
            mapDataToPixels(
                points = data.listOfPoints,
                canvasSize = size,
                graphTopPadding = graphTopPadding.toPx(),
                graphBottomPadding = graphBottomPadding.toPx(),
                minX = globalMinX,
                maxX = globalMaxX,
                minY = globalMinY,
                maxY = globalMaxY,
            )
        val connectionPoints = calculateConnectionPointsForBezierCurve(mappedPoints)

        path.reset()
        mappedPoints.forEachIndexed { index, value ->
            if (index == 0) {
                path.moveTo(value.x, value.y)
            } else {
                path.cubicTo(
                    connectionPoints[index - 1].first.x,
                    connectionPoints[index - 1].first.y,
                    connectionPoints[index - 1].second.x,
                    connectionPoints[index - 1].second.y,
                    value.x,
                    value.y,
                )
            }
        }

        if (data.lineWidth > 0.dp) {
            drawPath(
                path = path,
                color = data.lineColor.copy(alpha[seriesIndex]),
                style = Stroke(
                    width = data.lineWidth.toPx(),
                    pathEffect = if (data.dashedLine) dashedPathEffect else null,
                ),
            )
        }
        path.lineTo(mappedPoints.last().x, size.height)
        path.lineTo(mappedPoints.first().x, size.height)
        drawPath(
            path = path,
            Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    data.fillColor.copy(alpha[seriesIndex] / 12),
                    data.fillColor.copy(alpha[seriesIndex] / 6),
                ),
                startY = path.getBounds().bottom,
                endY = path.getBounds().top,
            ),
            style = Fill,
        )
    }
}

private fun mapDataToPixels(
    lineChartData: LineChartData,
    currentSeries: LineChartSeries,
    canvasSize: Size,
    graphTopPadding: Float = 0f,
    graphBottomPadding: Float,
): List<PointF> {
    val mappedPoints = currentSeries.listOfPoints.map {
        val x = it.x.mapValueToDifferentRange(
            lineChartData.minX,
            lineChartData.maxX,
            0f,
            canvasSize.width,
        )
        val y = it.y.mapValueToDifferentRange(
            lineChartData.minY,
            lineChartData.maxY,
            canvasSize.height - graphBottomPadding,
            graphTopPadding,
        )
        PointF(x, y)
    }
    return mappedPoints
}

private fun mapDataToPixels(
    points: List<LineChartPoint>,
    canvasSize: Size,
    graphTopPadding: Float,
    graphBottomPadding: Float,
    minX: Float,
    maxX: Float,
    minY: Float,
    maxY: Float,
): List<PointF> {
    return points.map { point ->
        val x = point.x.mapValueToDifferentRange(
            minX,
            maxX,
            0f,
            canvasSize.width,
        )
        val y = point.y.mapValueToDifferentRange(
            minY,
            maxY,
            canvasSize.height - graphBottomPadding,
            graphTopPadding,
        )
        PointF(x, y)
    }
}

private fun calculateConnectionPointsForBezierCurve(points: List<PointF>): MutableList<Pair<PointF, PointF>> {
    val conPoint = mutableListOf<Pair<PointF, PointF>>()
    for (i in 1 until points.size) {
        conPoint.add(
            Pair(
                PointF((points[i].x + points[i - 1].x) / 2f, points[i - 1].y),
                PointF((points[i].x + points[i - 1].x) / 2f, points[i].y),
            ),
        )
    }
    return conPoint
}

data class PointF(val x: Float, val y: Float)
