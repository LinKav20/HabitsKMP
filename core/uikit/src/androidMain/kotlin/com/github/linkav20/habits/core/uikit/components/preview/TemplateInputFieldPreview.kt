package com.github.linkav20.habits.core.uikit.components.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.input.HabitInputField

@Preview
@Composable
internal fun HabitInputFieldPreview() {
    HabitTheme {
        Column {
            HabitInputField(
                value = "Hello world",
                onValueChange = {},
                label = "label",
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitInputField(
                value = "",
                onValueChange = {},
                placeholder = "Placeholder",
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitInputField(
                value = "",
                onValueChange = {},
                placeholder = "Placeholder",
                isEnabled = false,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitInputField(
                value = "Hello world",
                onValueChange = {},
                label = "label",
                isEnabled = false,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitInputField(
                value = "Hello world",
                onValueChange = {},
                label = "label",
                isReadOnly = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitInputField(
                value = "Hello world",
                onValueChange = {},
                label = "label",
                isError = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitInputField(
                value = "Hello world",
                onValueChange = {},
                label = "label",
                isError = true,
                errorText = "Error!",
            )
        }
    }
}
