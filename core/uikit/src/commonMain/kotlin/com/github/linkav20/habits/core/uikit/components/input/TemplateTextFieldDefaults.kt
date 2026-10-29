package com.github.linkav20.habits.core.uikit.components.input

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.github.linkav20.habits.core.uikit.HabitTheme

object TemplateTextFieldDefaults {
    @Suppress("LongParameterList")
    @Composable
    fun colors(
        enabledTextColor: Color = HabitTheme.colors.textPrimary,
        disabledTextColor: Color = HabitTheme.colors.textDisabled,
        enabledLabelColor: Color = HabitTheme.colors.textSecondary,
        disabledLabelColor: Color = HabitTheme.colors.textDisabled,
        errorLabelColor: Color = HabitTheme.colors.textError,
        enabledPlaceholderColor: Color = enabledLabelColor,
        disabledPlaceholderColor: Color = disabledLabelColor,
        focusedBorderColor: Color = HabitTheme.colors.borderAccentPrimary,
        unfocusedBorderColor: Color = HabitTheme.colors.borderBasic,
        disabledBorderColor: Color = unfocusedBorderColor,
        errorBolderColor: Color = HabitTheme.colors.borderError,
        backgroundColor: Color = HabitTheme.colors.backgroundTertiary,
        cursorColor: Color = HabitTheme.colors.textAccentPrimary,
        disabledBackgroundColor: Color = HabitTheme.colors.backgroundTertiary,
        errorCursorColor: Color = HabitTheme.colors.textError,
    ): TemplateInputFieldColors {
        return TemplateInputFieldColors(
            focusedTextColor = enabledTextColor,
            unfocusedTextColor = enabledTextColor,
            disabledTextColor = disabledTextColor,
            enabledLabelColor = enabledLabelColor,
            errorLabelColor = errorLabelColor,
            enabledPlaceholderColor = enabledPlaceholderColor,
            disabledPlaceholderColor = disabledPlaceholderColor,
            disabledLabelColor = disabledLabelColor,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            disabledBorderColor = disabledBorderColor,
            errorBolderColor = errorBolderColor,
            backgroundColor = backgroundColor,
            cursorColor = cursorColor,
            disabledBackgroundColor = disabledBackgroundColor,
            errorCursorColor = errorCursorColor,
        )
    }
}

@Stable
data class TemplateInputFieldColors(
    val focusedTextColor: Color,
    val unfocusedTextColor: Color,
    val disabledTextColor: Color,
    val enabledLabelColor: Color,
    val disabledLabelColor: Color,
    val errorLabelColor: Color,
    val enabledPlaceholderColor: Color,
    val disabledPlaceholderColor: Color,
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val disabledBorderColor: Color,
    val errorBolderColor: Color,
    val backgroundColor: Color,
    val disabledBackgroundColor: Color,
    val cursorColor: Color,
    val errorCursorColor: Color,
) {
    @Composable
    fun asTextFieldColors(
        isReadOnly: Boolean = false,
        style: HabitInputFieldStyle = HabitInputFieldStyle.PLAIN,
    ): TextFieldColors {
        return TextFieldDefaults.colors(
            focusedTextColor = focusedTextColor,
            unfocusedTextColor = if (isReadOnly) enabledLabelColor else unfocusedTextColor,
            disabledTextColor = disabledTextColor,
            focusedLabelColor = enabledLabelColor,
            unfocusedLabelColor = enabledLabelColor,
            disabledLabelColor = disabledLabelColor,
            errorLabelColor = errorLabelColor,
            focusedPlaceholderColor = enabledPlaceholderColor,
            unfocusedPlaceholderColor = enabledPlaceholderColor,
            disabledPlaceholderColor = disabledPlaceholderColor,
            errorPlaceholderColor = enabledPlaceholderColor,
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            cursorColor = cursorColor,
            errorCursorColor = errorCursorColor,
            focusedIndicatorColor = if (style == HabitInputFieldStyle.UNDERLINED && !isReadOnly) {
                focusedBorderColor
            } else {
                Color.Transparent
            },
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    }
}
