package com.github.linkav20.habit.createitem.impl.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.utils.toCapitalizedString
import kotlinx.datetime.DayOfWeek

@Composable
fun RepeatDays(
    selectedDays: Set<DayOfWeek>,
    onDayClick: (Set<DayOfWeek>) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DayOfWeek.entries.forEach { day ->
            if (day in selectedDays) {
                SelectedDay(
                    dayOfWeek = day,
                    modifier = Modifier.weight(1f),
                    onUnselectDay = { onDayClick(selectedDays.minus(day)) },
                )
            } else {
                UnselectedDay(
                    dayOfWeek = day,
                    modifier = Modifier.weight(1f),
                    onSelectDay = { onDayClick(selectedDays.plus(day)) },
                )
            }
        }
    }
}

@Composable
private fun SelectedDay(
    dayOfWeek: DayOfWeek,
    onUnselectDay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = HabitTheme.colors.background,
                shape = HabitTheme.shapes.medium,
            ).clip(HabitTheme.shapes.medium)
            .clickable { onUnselectDay() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = dayOfWeek.toCapitalizedString(),
            style = HabitTheme.typography.body.copy(
                fontWeight = FontWeight.ExtraBold,
            ),
            color = HabitTheme.colors.textAccentPrimary,
        )
    }
}

@Composable
private fun UnselectedDay(
    dayOfWeek: DayOfWeek,
    onSelectDay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = HabitTheme.colors.backgroundTertiary,
                shape = HabitTheme.shapes.medium,
            ).clip(HabitTheme.shapes.medium)
            .clickable { onSelectDay() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = dayOfWeek.toCapitalizedString(),
            style = HabitTheme.typography.body,
            color = HabitTheme.colors.textPrimary,
        )
    }
}

