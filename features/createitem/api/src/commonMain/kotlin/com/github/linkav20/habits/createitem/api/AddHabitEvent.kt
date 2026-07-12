package com.github.linkav20.habits.createitem.api

import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import com.github.linkav20.habits.createitem.api.model.CreationForm
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

sealed interface AddHabitEvent {
    data object OnBackCLick : AddHabitEvent
    data class OpenNewCreationForm(val creationForm: CreationForm) : AddHabitEvent

    data class UpdateHabitTitle(val value: String) : AddHabitEvent
    data class UpdateHabitRepeatDays(val days: Set<DayOfWeek>) : AddHabitEvent
    data class UpdateHabitDeadline(val deadline: LocalDate?) : AddHabitEvent
    data class UpdateHabitEmoji(val emoji: String) : AddHabitEvent

    data class UpdateTodoTitle(val value: String) : AddHabitEvent
    data class UpdateTodoRepeatPeriod(val repeatPeriod: AddTodoForm.RepeatPeriod) : AddHabitEvent
    data class UpdateTodoDeadline(val deadline: LocalDate) : AddHabitEvent
    data class UpdateTodoStartDate(val startDate: LocalDate) : AddHabitEvent
    data object UpdateTodoIsRegular : AddHabitEvent

    data object CreateForm : AddHabitEvent
}
