package com.github.linkav20.habits.core.uikit.components.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.linkav20.habits.common.model.NetworkException
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.error.TemplateFullscreenError

@Composable
@Preview(showBackground = true)
internal fun HabitFullscreenErrorPreview() {
    HabitTheme {
        TemplateFullscreenError(
            exception = NetworkException.Connection,
            onActionClick = {},
        )
    }
}
