package com.github.linkav20.habits.core.uikit.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScrollUpFab(
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = { scrollUp(scope = scope, listState = listState) },
        modifier = modifier.padding(16.dp),
        containerColor = HabitTheme.colors.surfaceContainer,
    ) {
        Icon(
            painter = painterResource(HabitIcons.SmallArrowUp_24),
            contentDescription = null,
            tint = HabitTheme.colors.iconAccentPrimary,
        )
    }
}

fun scrollUp(
    scope: CoroutineScope,
    listState: LazyListState,
) {
    val animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing,
    )
    scope.launch {
        if (listState.firstVisibleItemIndex > 3) {
            listState.scrollToItem(3)
        }
        val itemSize = listState.layoutInfo.visibleItemsInfo
            .takeIf { it.isNotEmpty() }
            ?.maxOf { it.size.toFloat() }
            ?: return@launch
        listState.animateScrollBy(
            value = -(3 * itemSize),
            animationSpec = animationSpec,
        )
    }
}
