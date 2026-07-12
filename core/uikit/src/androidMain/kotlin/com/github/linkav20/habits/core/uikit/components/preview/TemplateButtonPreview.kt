package com.github.linkav20.habits.core.uikit.components.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonSize
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonState
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonType

@Suppress("UnusedPrivateMember", "LongMethod")
@Preview(showBackground = true)
@Composable
private fun HabitButtonPreview() {
    HabitTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.L,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.L,
                    state = HabitButtonState.DISABLED,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.L,
                    state = HabitButtonState.LOADING,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.M,
                    type = HabitButtonType.OUTLINE,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    type = HabitButtonType.OUTLINE,
                    state = HabitButtonState.DISABLED,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    type = HabitButtonType.OUTLINE,
                    state = HabitButtonState.LOADING,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.S,
                    type = HabitButtonType.GHOST,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.S,
                    type = HabitButtonType.GHOST,
                    state = HabitButtonState.DISABLED,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    size = HabitButtonSize.S,
                    type = HabitButtonType.GHOST,
                    state = HabitButtonState.LOADING,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    contentColor = HabitTheme.colors.textPrimary,
                    containerColor = HabitTheme.colors.textSuccess,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    contentColor = HabitTheme.colors.textSuccess,
                    size = HabitButtonSize.S,
                    type = HabitButtonType.GHOST,
                )
                HabitButton(
                    onClick = {},
                    title = "Hello",
                    contentColor = HabitTheme.colors.textSuccess,
                    size = HabitButtonSize.S,
                    type = HabitButtonType.OUTLINE,
                    state = HabitButtonState.LOADING,
                )
            }
        }
    }
}
