package com.github.linkav20.habits.core.uikit.components.radiobutton

import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import com.github.linkav20.habits.core.uikit.HabitTheme

object HabitRadioButtonDefaults {
    @Composable
    fun colors(): RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = HabitTheme.colors.borderAccentPrimary,
        unselectedColor = HabitTheme.colors.borderBasic,
        disabledSelectedColor = HabitTheme.colors.borderDisabled,
        disabledUnselectedColor = HabitTheme.colors.borderDisabled,
    )
}
