package com.github.linkav20.habits.core.uikit.components.slider

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme

@Composable
fun HabitSeekBar(
    modifier: Modifier = Modifier,
    progress: Float,
    maxProgress: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    thumbRadius: Dp = 6.dp,
    trackHeight: Dp = 4.dp,
    activeTrackColor: Color = HabitTheme.colors.iconAccentPrimary,
    inactiveTrackColor: Color = HabitTheme.colors.backgroundSecondary,
    thumbColor: Color = HabitTheme.colors.iconInverse,
    rippleColor: Color = HabitTheme.colors.iconInverse.copy(alpha = 0.3f),
    showThumbAlways: Boolean = false
) {
    val density = LocalDensity.current
    var isDragging by remember { mutableStateOf(false) }
    var localProgress by remember { mutableStateOf(progress) }
    var trackWidth by remember { mutableStateOf(1f) }
    var dragStartOffsetX by remember { mutableStateOf(0f) }
    var initialProgress by remember { mutableStateOf(0f) }

    val rippleRadius by animateFloatAsState(
        targetValue = if (isDragging) with(density) { (thumbRadius * 2f).toPx() } else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "Ripple Animation",
    )

    LaunchedEffect(progress, maxProgress) {
        if (!isDragging) {
            localProgress = progress
        }
    }

    Box(
        modifier = modifier
            .height(thumbRadius * 3)
            .onSizeChanged { newSize -> trackWidth = newSize.width.toFloat() }
            .pointerInput(maxProgress) {
                detectTapGestures { offset ->
                    val newValue = (offset.x / trackWidth) * maxProgress
                    localProgress = newValue.coerceIn(0f, maxProgress)
                    onValueChange(localProgress)
                    onValueChangeFinished?.invoke()
                }
            }
            .pointerInput(maxProgress) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragStartOffsetX = offset.x
                        initialProgress = (dragStartOffsetX / trackWidth) * maxProgress
                        localProgress = initialProgress.coerceIn(0f, maxProgress)
                        onValueChange(localProgress)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val dragDelta = (dragAmount.x / trackWidth) * maxProgress
                        localProgress = (localProgress + dragDelta).coerceIn(0f, maxProgress)
                        onValueChange(localProgress)
                    },
                    onDragEnd = {
                        isDragging = false
                        onValueChangeFinished?.invoke()
                    },
                    onDragCancel = { isDragging = false },
                )
            },
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.CenterStart),
        ) {
            val trackWidthPx = size.width
            val thumbPx = with(density) { thumbRadius.toPx() }
            val progressX = (localProgress / maxProgress) * trackWidthPx

            drawLine(
                color = inactiveTrackColor,
                cap = StrokeCap.Round,
                start = Offset(0f, size.height / 2),
                end = Offset(trackWidthPx, size.height / 2),
                strokeWidth = with(density) { trackHeight.toPx() },
            )

            drawLine(
                color = activeTrackColor,
                cap = StrokeCap.Round,
                start = Offset(0f, size.height / 2),
                end = Offset(progressX, size.height / 2),
                strokeWidth = with(density) { trackHeight.toPx() },
            )

            if (isDragging && rippleRadius > 0f) {
                drawCircle(
                    color = rippleColor,
                    radius = rippleRadius,
                    center = Offset(progressX, size.height / 2),
                )
            }

            if (isDragging || showThumbAlways) {
                drawCircle(
                    color = thumbColor,
                    radius = thumbPx,
                    center = Offset(progressX, size.height / 2),
                )
                drawCircle(
                    color = inactiveTrackColor,
                    radius = thumbPx,
                    center = Offset(progressX, size.height / 2),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}


