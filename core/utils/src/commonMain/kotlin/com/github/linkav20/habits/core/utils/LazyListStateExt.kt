package com.github.linkav20.habits.core.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

private const val SCROLL_THRESHOLD = 150

@Composable
public fun LazyListState.isScrollingUp(): Boolean {
    var isScrollingUp by remember { mutableStateOf(false) }

    var prevIndex by remember { mutableIntStateOf(0) }
    var prevOffset by remember { mutableIntStateOf(0) }
    var accumulatedScroll by remember { mutableIntStateOf(0) }

    LaunchedEffect(this) {
        snapshotFlow { firstVisibleItemIndex to firstVisibleItemScrollOffset }
            .collect { (currIndex, currOffset) ->

                if (currIndex == 0) {
                    isScrollingUp = false
                    prevIndex = currIndex
                    prevOffset = currOffset
                    accumulatedScroll = 0
                    return@collect
                }

                val scrollingUp = currIndex < prevIndex ||
                        (currIndex == prevIndex && currOffset < prevOffset)
                val scrollingDown = currIndex > prevIndex ||
                        (currIndex == prevIndex && currOffset > prevOffset)

                if (scrollingUp) {
                    val delta = (prevOffset - currOffset).coerceAtLeast(0)
                    accumulatedScroll += delta
                    if (accumulatedScroll > SCROLL_THRESHOLD) {
                        isScrollingUp = true
                        accumulatedScroll = 0
                    }
                } else if (scrollingDown) {
                    val delta = (currOffset - prevOffset).coerceAtLeast(0)
                    accumulatedScroll += delta
                    if (accumulatedScroll > SCROLL_THRESHOLD) {
                        isScrollingUp = false
                        accumulatedScroll = 0
                    }
                }

                prevIndex = currIndex
                prevOffset = currOffset
            }
    }

    return isScrollingUp
}
