package com.github.linkav20.habits.core.uikit.components.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberTemplateSnackBarHostState(): TemplateSnackBarHostState {
    return remember { TemplateSnackBarHostState() }
}

@Composable
fun TemplateSnackBarHost(
    hostState: TemplateSnackBarHostState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBars = hostState.currentTemplateSnackBarData
    snackBars.forEach { data ->
        LaunchedEffect(data) {
            delay(data.duration.toMillis())
            coroutineScope.launch {
                data.visible.targetState = false
                delay(500)
                hostState.currentTemplateSnackBarData =
                    hostState.currentTemplateSnackBarData.toMutableList().apply {
                        remove(data)
                    }
            }
        }
    }

    if (snackBars.isNotEmpty()) {
        TemplateSnackBar(
            snackBarData = snackBars.toImmutableList(),
            onClick = { data ->
                coroutineScope.launch {
                    data.visible.targetState = false
                    delay(500)
                    hostState.currentTemplateSnackBarData =
                        hostState.currentTemplateSnackBarData.toMutableList().apply {
                            remove(data)
                        }
                }
            },
            modifier = modifier,
        )
    }
}

@Stable
class TemplateSnackBarHostState {
    internal var currentTemplateSnackBarData by mutableStateOf<List<HabitSnackBarData>>(emptyList())

    fun showSnackBar(data: HabitSnackBarData) {
        if (currentTemplateSnackBarData.none { it.deduplicateId == data.deduplicateId }) {
            currentTemplateSnackBarData = currentTemplateSnackBarData.toMutableList().apply { add(data) }
        }
    }
}

private fun HabitSnackBarDuration.toMillis(): Long =
    when (this) {
        HabitSnackBarDuration.Short -> 4000L
        HabitSnackBarDuration.Long -> 10000L
        HabitSnackBarDuration.Indefinite -> Long.MAX_VALUE
    }
