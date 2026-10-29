package com.github.linkav20.habits.core.uikit.components.chip

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme

@Composable
fun HabitFilterChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 36.dp),
        shape = HabitTheme.shapes.medium,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = HabitTheme.colors.backgroundTertiary,
            labelColor = HabitTheme.colors.textSecondary,
            selectedContainerColor = HabitTheme.colors.background,
            selectedLabelColor = HabitTheme.colors.textAccentPrimary,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = enabled,
            selected = selected,
            borderColor = Color.Transparent,
            selectedBorderColor = HabitTheme.colors.borderAccentPrimary,
            selectedBorderWidth = 1.5.dp,
        ),
        label = {
            Text(
                text = title,
                style = HabitTheme.typography.textSmall,
            )
        },
    )
}
