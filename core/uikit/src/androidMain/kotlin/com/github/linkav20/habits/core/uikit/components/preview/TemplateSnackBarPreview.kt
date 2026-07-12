package com.github.linkav20.habits.core.uikit.components.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.snackbar.HabitSnackBarData
import com.github.linkav20.habits.core.uikit.components.snackbar.TemplateSnackBarItem
import com.github.linkav20.habits.core.uikit.components.snackbar.HabitSnackBarType

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun HabitSnackBarPreview() {
    HabitTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            TemplateSnackBarItem(
                HabitSnackBarData(
                    titleText = "Example",
                    messageText = "При загрузке фотографии произошла ошибка. " +
                            "Пожалуйста, повтори попытку, или попробуй выбрать фотографию меньшего размера",
                    type = HabitSnackBarType.SUCCESS,
                ),
                {},
            )

            TemplateSnackBarItem(
                HabitSnackBarData(
                    titleText = "Example",
                    messageText = "При загрузке фотографии произошла ошибка. " +
                            "Пожалуйста, повтори попытку, или попробуй выбрать фотографию меньшего размера",
                    type = HabitSnackBarType.INFO,
                ),
                {},
            )

            TemplateSnackBarItem(
                HabitSnackBarData(
                    titleText = "Example",
                    messageText = "При загрузке фотографии произошла ошибка. " +
                            "Пожалуйста, повтори попытку, или попробуй выбрать фотографию меньшего размера",
                    type = HabitSnackBarType.ERROR,
                ),
                {},
            )

            TemplateSnackBarItem(
                HabitSnackBarData(
                    titleText = "Example",
                    messageText = "При загрузке фотографии произошла ошибка. " +
                            "Пожалуйста, повтори попытку, или попробуй выбрать фотографию меньшего размера",
                    type = HabitSnackBarType.WARNING,
                ),
                {},
            )
        }
    }
}
