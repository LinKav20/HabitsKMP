package com.github.linkav20.habits.createitem.api

import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.createitem.api.model.CreationForm
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

sealed interface AddHabitIntent {
    data class OpenNewCreationForm(val creationForm: CreationForm) : AddHabitIntent
    data class UpdateHabitTitle(val value: String) : AddHabitIntent
    data class UpdateHabitRepeatDays(val days: Set<DayOfWeek>) : AddHabitIntent
    data class UpdateHabitDeadline(val deadline: LocalDate?) : AddHabitIntent
    data class UpdateHabitEmoji(val emoji: String) : AddHabitIntent

    data class UpdateTodoTitle(val value: String) : AddHabitIntent
    data class UpdateTodoRepeatPeriod(val repeatPeriod: AddTodoForm.RepeatPeriod) : AddHabitIntent
    data class UpdateTodoDeadline(val deadline: LocalDate) : AddHabitIntent
    data class UpdateTodoStartDate(val startDate: LocalDate) : AddHabitIntent
    data object UpdateTodoIsRegular : AddHabitIntent

    data object CreateForm : AddHabitIntent
}
