package com.github.linkav20.habits.core.uikit.components.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.search.TemplateSearchBox

@Preview
@Composable
internal fun HabitSearchBoxPreview() {
    HabitTheme {
        Column {
            TemplateSearchBox(
                onValueChange = {},
                placeholder = "Поиск по курсам",
            )
            Spacer(modifier = Modifier.height(16.dp))
            TemplateSearchBox(
                onValueChange = {},
                placeholder = "Поиск по курсам",
                isEnabled = false,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TemplateSearchBox(
                onValueChange = {},
                placeholder = "Поиск по курсам",
                isError = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TemplateSearchBox(
                onValueChange = {},
                placeholder = "Поиск по курсам",
                isError = true,
                errorText = "Error",
            )
            Spacer(modifier = Modifier.height(16.dp))
            TemplateSearchBox(
                onValueChange = {},
                placeholder = "Поиск по курсам",
                isError = true,
                errorText = "Error",
                isEnabled = false,
            )
        }
    }
}
