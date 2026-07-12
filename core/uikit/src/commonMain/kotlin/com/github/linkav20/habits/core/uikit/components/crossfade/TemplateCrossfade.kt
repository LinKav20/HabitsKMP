package com.github.linkav20.habits.core.uikit.components.crossfade

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val ANIMATION_DURATION_MS = 300

@Composable
fun <T> TemplateCrossfade(
    state: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = ANIMATION_DURATION_MS),
    label: String = "TemplateCrossfade",
    content: @Composable (T) -> Unit,
) {
    Crossfade(
        modifier = modifier,
        animationSpec = animationSpec,
        label = label,
        targetState = state,
        content = content,
    )
}
