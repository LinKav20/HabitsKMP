package com.github.linkav20.habit.createitem.impl.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.dayselector.HabitDayPicker
import com.github.linkav20.habits.core.uikit.components.input.HabitInputField
import com.github.linkav20.habits.core.uikit.components.segmentedbutton.HabitSegmentedButton
import com.github.linkav20.habits.core.uikit.components.switch.HabitSwitch
import com.github.linkav20.habits.core.uikit.emojipicker.EmojiPicker
import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.createitem.api.model.AddHabitForm
import com.github.linkav20.habits.createitem.impl.resources.Res
import com.github.linkav20.habits.createitem.impl.resources.add_habit_endless_completion_day
import com.github.linkav20.habits.createitem.impl.resources.add_habit_endless_days_until_end
import com.github.linkav20.habits.createitem.impl.resources.add_habit_endless_days_until_end_placeholder
import com.github.linkav20.habits.createitem.impl.resources.add_habit_endless_title
import com.github.linkav20.habits.createitem.impl.resources.add_habit_habit_title_placeholder
import com.github.linkav20.habits.createitem.impl.resources.add_habit_repeat_days_title
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.DateTimeUnit.Companion.MONTH
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddHabitFromContent(
    habitForm: AddHabitForm,
    onHabitTitleChanged: (String) -> Unit,
    onHabitRepeatDaysChanged: (Set<DayOfWeek>) -> Unit,
    onHabitDeadlineChanged: (LocalDate?) -> Unit,
    onHabitEmojiChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showEmojiDialog by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(42.dp),
        ) {
            HabitFormTitle(
                title = habitForm.title,
                emoji = habitForm.emoji,
                onTitleUpdated = onHabitTitleChanged,
                onOpenEmojiPicker = { showEmojiDialog = true },
            )
            HabitFormRepeatDays(
                repeatDays = habitForm.repeatDays,
                onDaysUpdated = onHabitRepeatDaysChanged,
            )
            EndlessHabit(
                deadline = habitForm.deadline,
                onDeadlineUpdated = onHabitDeadlineChanged,
            )
        }
        if (showEmojiDialog) {
            EmojiPicker(
                modifier = Modifier.size(300.dp),
                onSelectEmoji = {
                    onHabitEmojiChanged(it)
                    showEmojiDialog = false
                },
                selectedEmoji = habitForm.emoji,
                onClose = { showEmojiDialog = false },
            )
        }
    }
}

@Composable
private fun EndlessHabit(
    deadline: LocalDate?,
    onDeadlineUpdated: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val baseDeadline = deadline ?: Time.now().plus(1, MONTH)
    var checked by rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.add_habit_endless_title),
                style = HabitTheme.typography.subtitle.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = HabitTheme.colors.textPrimary,
            )
            HabitSwitch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    if (it) {
                        onDeadlineUpdated(null)
                    } else {
                        onDeadlineUpdated(baseDeadline)
                    }
                },
            )
        }
        if (checked.not()) {
            SelectHabitDeadline(
                deadline = baseDeadline,
                onDeadlineUpdated = onDeadlineUpdated,
            )
        }
    }
}

@Composable
private fun SelectHabitDeadline(
    deadline: LocalDate,
    onDeadlineUpdated: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        val completionDay = stringResource(Res.string.add_habit_endless_completion_day)
        val daysUntil = stringResource(Res.string.add_habit_endless_days_until_end)
        var select by rememberSaveable { mutableStateOf(completionDay) }
        HabitSegmentedButton(
            selectedOption = select,
            options = setOf(
                completionDay,
                daysUntil,
            ),
            onOptionSelected = { select = it },
        )
        when (select) {
            completionDay -> {
                HabitDayPicker(
                    initialDate = deadline,
                    onDateSelected = { onDeadlineUpdated(it) },
                    modifier = Modifier.fillMaxWidth(),
                    lowDataBound = Time.now(),
                )
            }

            daysUntil -> {
                var days by rememberSaveable {
                    mutableStateOf(
                        Time.daysBetween(Time.now(), deadline).toString(),
                    )
                }
                HabitInputField(
                    value = days,
                    placeholder = stringResource(Res.string.add_habit_endless_days_until_end_placeholder),
                    onValueChange = { newValue ->
                        days = newValue
                        val filtered = newValue.filter { it.isDigit() }
                        days = filtered

                        val daysInt = filtered.toIntOrNull()
                        if (daysInt != null && daysInt >= 0) {
                            onDeadlineUpdated(Time.now().plus(daysInt.toLong(), DAY))
                        } else {
                            onDeadlineUpdated(null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                )
            }
        }
    }
}

@Composable
private fun HabitFormTitle(
    title: String,
    emoji: String,
    onTitleUpdated: (String) -> Unit,
    onOpenEmojiPicker: () -> Unit,
) {
    Box {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HabitInputField(
                value = title,
                placeholder = stringResource(Res.string.add_habit_habit_title_placeholder),
                onValueChange = onTitleUpdated,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight()
                    .background(
                        color = HabitTheme.colors.backgroundTertiary,
                        shape = HabitTheme.shapes.medium,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.clickable { onOpenEmojiPicker() },
                    text = emoji,
                    style = HabitTheme.typography.subtitle,
                )
            }
        }
    }
}

@Composable
private fun HabitFormRepeatDays(
    repeatDays: Set<DayOfWeek>,
    onDaysUpdated: (Set<DayOfWeek>) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = stringResource(Res.string.add_habit_repeat_days_title),
            style = HabitTheme.typography.subtitle.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = HabitTheme.colors.textPrimary,
        )
        RepeatDays(
            selectedDays = repeatDays,
            onDayClick = { onDaysUpdated(it) },
        )
    }
}
