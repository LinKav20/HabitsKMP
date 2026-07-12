package com.github.linkav20.habits.core.uikit.components.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.topbar.HabitTopBar
import com.github.linkav20.habits.core.uikit.components.topbar.TemplateTopBarSearch

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun HabitTopBarPreviewDark() {
    HabitTheme {
        HabitTopBar(
            title = "title",
            subtitle = "subtitle",
            onBackClick = {},
            search = TemplateTopBarSearch(
                value = "",
                onValueChanged = {},
            ),
        )
    }
}

@Composable
@Preview
internal fun HabitTopBarPreviewLight() {
    HabitTheme {
        HabitTopBar(
            title = "title",
            subtitle = "subtitle",
            onBackClick = {},
        )
    }
}
