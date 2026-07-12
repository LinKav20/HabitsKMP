package com.github.linkav20.habits.core.uikit.components.segmentedbutton

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme

enum class Type {
    PRIMARY,
    OUTLINE,
}

@Composable
fun HabitSegmentedButton(
    selectedOption: String,
    options: Set<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    type: Type = Type.PRIMARY,
) {
    if (selectedOption !in options) {
        println("HabitSegmentedButton: selected option not in options set")
        return
    }
    var selectedIndex by remember { mutableStateOf(options.indexOf(selectedOption)) }
    var buttonWidth by remember { mutableStateOf(0.dp) }
    var buttonOffsets by remember { mutableStateOf(mapOf<Int, Dp>()) }
    val density = LocalDensity.current

    LaunchedEffect(selectedOption) {
        selectedIndex = options.indexOf(selectedOption)
    }

    val animatedOffset by animateDpAsState(
        targetValue = buttonOffsets[selectedIndex] ?: 0.dp,
        animationSpec = tween(durationMillis = 300),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .align(Alignment.Center)
                    .background(
                        color = when (type) {
                            Type.PRIMARY -> HabitTheme.colors.backgroundTertiary
                            Type.OUTLINE -> Color.Transparent
                        },
                        shape = HabitTheme.shapes.medium,
                    )
                    .then(
                        when (type) {
                            Type.PRIMARY -> Modifier
                            Type.OUTLINE -> Modifier.border(
                                width = 1.5.dp,
                                color = HabitTheme.colors.borderAccentPrimary,
                                shape = HabitTheme.shapes.medium,
                            )
                        },
                    )
                    .padding(4.dp),
            )
        }

        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .fillMaxHeight()
                .width(buttonWidth)
                .background(
                    color = HabitTheme.colors.primaryContainer,
                    shape = HabitTheme.shapes.medium,
                ).border(
                    width = 1.5.dp,
                    color = HabitTheme.colors.borderAccentPrimary,
                    shape = HabitTheme.shapes.medium,
                ),
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .onGloballyPositioned { coordinates ->
                            if (buttonWidth == 0.dp) {
                                buttonWidth = with(density) { coordinates.size.width.toDp() }
                            }
                            val position = with(density) { coordinates.positionInParent().x.toDp() }
                            buttonOffsets = buttonOffsets + (index to position)
                        }
                        .clickable {
                            selectedIndex = index
                            onOptionSelected(option)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) {
                            HabitTheme.colors.textAccentPrimary
                        } else {
                            when (type) {
                                Type.PRIMARY -> HabitTheme.colors.textSecondary
                                Type.OUTLINE -> HabitTheme.colors.textAccentPrimary
                            }
                        },
                        style = if (isSelected) {
                            HabitTheme.typography.body.copy(
                                fontWeight = FontWeight.ExtraBold,
                            )
                        } else {
                            HabitTheme.typography.body
                        },
                    )
                }
            }
        }
    }
}
