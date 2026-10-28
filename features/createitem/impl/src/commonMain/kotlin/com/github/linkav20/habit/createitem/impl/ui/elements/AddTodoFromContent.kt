package com.github.linkav20.habit.createitem.impl.ui.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.radiobutton.HabitRadioButtonDefaults
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.button.HabitButtonType
import com.github.linkav20.habits.core.uikit.components.dayselector.HabitDayPicker
import com.github.linkav20.habits.core.uikit.components.dayselector.SelectData
import com.github.linkav20.habits.core.uikit.components.input.HabitInputField
import com.github.linkav20.habits.core.uikit.components.segmentedbutton.HabitSegmentedButton
import com.github.linkav20.habits.core.uikit.components.switch.HabitSwitch
import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.core.utils.fullDate
import com.github.linkav20.habits.core.utils.toDay
import com.github.linkav20.habits.core.utils.toMonthDay
import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.createitem.impl.resources.Res
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_deadline_title
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_regular_monthly
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_regular_title
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_regular_weekly
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_regular_yearly
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_start_day_subtitle
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_start_day_title
import com.github.linkav20.habits.createitem.impl.resources.add_habit_todo_title_placeholder
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddTodoFromContent(
    todoForm: AddTodoForm,
    onTitleUpdated: (String) -> Unit,
    onDeadlineUpdated: (LocalDate) -> Unit,
    onStartDateChanged: (LocalDate) -> Unit,
    onChangeRegular: () -> Unit,
    onChangeRepeatPeriod: (AddTodoForm.RepeatPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(42.dp),
    ) {
        HabitInputField(
            value = todoForm.title,
            placeholder = stringResource(Res.string.add_habit_todo_title_placeholder),
            onValueChange = onTitleUpdated,
        )
        DeadlineBlock(
            deadline = todoForm.deadline,
            onDeadlineUpdated = onDeadlineUpdated,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.add_habit_todo_regular_title),
                style = HabitTheme.typography.subtitle.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = HabitTheme.colors.textPrimary,
            )
            HabitSwitch(
                checked = todoForm.isRegular,
                onCheckedChange = { onChangeRegular() },
            )
        }
        when (todoForm.repeatPeriod) {
            is AddTodoForm.RepeatPeriod.Single -> SelectTodoStartDate(
                startDate = (todoForm.repeatPeriod as AddTodoForm.RepeatPeriod.Single).startDate,
                onStartDateChanged = onStartDateChanged,
            )

            else -> SelectRepeatDays(
                repeatPeriod = todoForm.repeatPeriod,
                onChangeRepeatPeriod = onChangeRepeatPeriod,
            )
        }
    }
}

@Composable
private fun SelectRepeatDays(
    repeatPeriod: AddTodoForm.RepeatPeriod,
    onChangeRepeatPeriod: (AddTodoForm.RepeatPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        val weekly = stringResource(Res.string.add_habit_todo_regular_weekly)
        val yearly = stringResource(Res.string.add_habit_todo_regular_yearly)
        val monthly = stringResource(Res.string.add_habit_todo_regular_monthly)
        HabitSegmentedButton(
            selectedOption = when (repeatPeriod) {
                is AddTodoForm.RepeatPeriod.Weekly -> weekly
                is AddTodoForm.RepeatPeriod.Monthly -> monthly
                is AddTodoForm.RepeatPeriod.Yearly -> yearly
                is AddTodoForm.RepeatPeriod.Single -> ""
            },
            options = setOf(
                weekly,
                monthly,
                yearly,
            ),
            onOptionSelected = {
                onChangeRepeatPeriod(
                    when (it) {
                        weekly -> AddTodoForm.RepeatPeriod.Weekly()
                        yearly -> AddTodoForm.RepeatPeriod.Yearly()
                        else -> AddTodoForm.RepeatPeriod.Monthly()
                    },
                )
            },
        )
        when (repeatPeriod) {
            is AddTodoForm.RepeatPeriod.Weekly -> RepeatDays(
                selectedDays = repeatPeriod.days,
                onDayClick = { onChangeRepeatPeriod(repeatPeriod.copy(days = it)) },
            )

            is AddTodoForm.RepeatPeriod.Monthly -> {
                SelectRepeatMonthly(
                    repeatPeriod = repeatPeriod,
                    onChangeUseFirstMonthDay = {
                        onChangeRepeatPeriod(repeatPeriod.copy(repeatOnFirstDay = repeatPeriod.repeatOnFirstDay.not()))
                    },
                    onChangeUseLastMonthDay = {
                        onChangeRepeatPeriod(repeatPeriod.copy(repeatOnLastDay = repeatPeriod.repeatOnLastDay.not()))
                    },
                    onChangeSpecificDay = { onChangeRepeatPeriod(it) },
                )
            }

            is AddTodoForm.RepeatPeriod.Yearly -> {
                SelectRepeatYearly(
                    repeatPeriod = repeatPeriod,
                    onChangeSpecificDay = { onChangeRepeatPeriod(it) },
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun SelectRepeatMonthly(
    repeatPeriod: AddTodoForm.RepeatPeriod.Monthly,
    onChangeUseFirstMonthDay: () -> Unit,
    onChangeUseLastMonthDay: () -> Unit,
    onChangeSpecificDay: (AddTodoForm.RepeatPeriod) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                colors = HabitRadioButtonDefaults.colors(),
                selected = repeatPeriod.repeatOnFirstDay,
                onClick = onChangeUseFirstMonthDay,
            )
            Text(
                modifier = Modifier.fillMaxWidth().weight(1f),
                text = "First day in month",
                color = HabitTheme.colors.textPrimary,
                style = HabitTheme.typography.body,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                colors = HabitRadioButtonDefaults.colors(),
                selected = repeatPeriod.repeatOnLastDay,
                onClick = onChangeUseLastMonthDay,
            )
            Text(
                modifier = Modifier.fillMaxWidth().weight(1f),
                text = "Last day in month",
                color = HabitTheme.colors.textPrimary,
                style = HabitTheme.typography.body,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                colors = HabitRadioButtonDefaults.colors(),
                selected = repeatPeriod.days != null,
                onClick = {
                    onChangeSpecificDay(
                        repeatPeriod.copy(
                            days = if (repeatPeriod.days == null) emptySet() else null,
                        ),
                    )
                },
            )
            Text(
                modifier = Modifier.fillMaxWidth().weight(1f),
                text = "Specific day in month",
                color = HabitTheme.colors.textPrimary,
                style = HabitTheme.typography.body,
            )
        }
        if (repeatPeriod.days != null) {
            AddDays(
                dates = repeatPeriod.daysAsLocalDate(),
                selectData = SelectData.DAY,
                modifier = Modifier.fillMaxWidth(),
                onDatesChanged = {
                    onChangeSpecificDay(
                        repeatPeriod.copy(
                            days = it.map { date -> date.day }.toSet(),
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun SelectRepeatYearly(
    repeatPeriod: AddTodoForm.RepeatPeriod.Yearly,
    onChangeSpecificDay: (AddTodoForm.RepeatPeriod) -> Unit,
) {
    AddDays(
        dates = repeatPeriod.datesAsLocalDate(),
        selectData = SelectData.DAY_MONTH,
        modifier = Modifier.fillMaxWidth(),
        onDatesChanged = {
            onChangeSpecificDay(
                repeatPeriod.copy(
                    dates = it.map { date ->
                        AddTodoForm.RepeatPeriod.Yearly.MonthDay(
                            month = date.month.number,
                            day = date.day,
                        )
                    }.toSet(),
                ),
            )
        },
    )
}

@Composable
private fun SelectTodoStartDate(
    startDate: LocalDate,
    onStartDateChanged: (LocalDate) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.add_habit_todo_start_day_title),
            style = HabitTheme.typography.subtitle.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = HabitTheme.colors.textPrimary,
        )
        Text(
            text = stringResource(Res.string.add_habit_todo_start_day_subtitle),
            style = HabitTheme.typography.textSmall,
            color = HabitTheme.colors.textSecondary,
        )
        HabitDayPicker(
            initialDate = startDate,
            onDateSelected = { onStartDateChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            lowDataBound = Time.now(),
        )
    }
}

@Composable
private fun DeadlineBlock(
    deadline: LocalDate,
    onDeadlineUpdated: (LocalDate) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.add_habit_todo_deadline_title),
            style = HabitTheme.typography.subtitle.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = HabitTheme.colors.textPrimary,
        )
        HabitDayPicker(
            initialDate = deadline,
            onDateSelected = { onDeadlineUpdated(it) },
            modifier = Modifier.fillMaxWidth(),
            lowDataBound = Time.now(),
        )
    }
}

@Composable
private fun AddDays(
    dates: Set<LocalDate>,
    selectData: SelectData,
    onDatesChanged: (Set<LocalDate>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showNewDayPicker by remember(dates) {
        mutableStateOf(dates.isEmpty())
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        dates.forEach { date ->
            AnimatedContent(date) { targetDate ->
                DeletableRow(
                    modifier = Modifier.fillMaxWidth(),
                    text = targetDate.toString(selectData),
                    onDeleteClick = { onDatesChanged(dates.minus(date)) },
                )
            }
        }
        if (showNewDayPicker) {
            var date by remember { mutableStateOf<LocalDate?>(null) }
            HabitDayPicker(
                initialDate = date,
                selectData = selectData,
                onDateSelected = { onDatesChanged(dates.plus(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            HabitButton(
                title = "Add",
                icon = HabitIcons.Add_24,
                type = HabitButtonType.TERTIARY,
                onClick = { showNewDayPicker = true },
            )
        }
    }
}

@Composable
private fun DeletableRow(
    text: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().height(56.dp)
            .background(
                color = HabitTheme.colors.backgroundTertiary,
                shape = HabitTheme.shapes.medium,
            )
            .clip(HabitTheme.shapes.medium)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = HabitTheme.colors.textPrimary,
                style = HabitTheme.typography.body.copy(
                    fontStyle = FontStyle.Italic,
                ),
            )
            Icon(
                modifier = Modifier.clickable { onDeleteClick() },
                painter = painterResource(HabitIcons.Delete_24),
                contentDescription = null,
                tint = HabitTheme.colors.iconSecondary,
            )
        }
    }
}

@Composable
private fun LocalDate.toString(selectData: SelectData): String {
    return when (selectData) {
        SelectData.DAY -> this.toDay()
        SelectData.DAY_MONTH -> this.toMonthDay()
        SelectData.DAY_MONTH_YEAR -> this.fullDate()
    }
}
