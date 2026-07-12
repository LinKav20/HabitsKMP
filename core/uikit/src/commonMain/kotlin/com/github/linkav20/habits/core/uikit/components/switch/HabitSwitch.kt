package com.github.linkav20.habits.core.uikit.components.switch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme

@Composable
fun HabitSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    switchWidth: Dp = 52.dp,
    switchHeight: Dp = 20.dp,
    thumbSize: Dp = 16.dp,
    thumbPadding: Dp = 2.dp,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) {
            HabitTheme.colors.iconAccentPrimary
        } else {
            HabitTheme.colors.backgroundTertiary
        },
        animationSpec = tween(durationMillis = 200),
    )

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) {
            switchWidth - thumbSize - thumbPadding * 2
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 200),
    )

    Box(
        modifier = modifier
            .width(switchWidth)
            .height(switchHeight)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(switchHeight / 2),
            )
            .then(
                if (enabled) {
                    Modifier.clickable { onCheckedChange(!checked) }
                } else {
                    Modifier
                },
            )
            .padding(thumbPadding),
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .background(
                    color = Color.White,
                    shape = CircleShape,
                ),
        )
    }
}
