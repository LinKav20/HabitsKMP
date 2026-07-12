package com.github.linkav20.habit.createitem.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.linkav20.habit.createitem.impl.domain.usecase.CreateItemUseCase
import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.createitem.api.AddHabitIntent
import com.github.linkav20.habits.createitem.api.AddHabitLabel
import com.github.linkav20.habits.createitem.api.AddHabitStore
import com.github.linkav20.habits.createitem.api.model.AddHabitForm
import com.github.linkav20.habits.createitem.api.model.AddTodoForm
import kotlinx.coroutines.launch

internal class AddHabitExecutor(
    private val createItemUseCase: CreateItemUseCase,
   // private val communication: GlobalCommunication,
) :
    CoroutineExecutor<AddHabitIntent, AddHabitAction, AddHabitStore.State, AddHabitMessage, AddHabitLabel>(
    ) {

    override fun executeAction(action: AddHabitAction) {
        super.executeAction(action)
        when (action) {
            else -> {}
        }
    }

    override fun executeIntent(intent: AddHabitIntent) {
        super.executeIntent(intent)
        when (intent) {
            is AddHabitIntent.OpenNewCreationForm -> {
                dispatch(AddHabitMessage.OpenNewCreationForm(intent.creationForm))
            }

            is AddHabitIntent.UpdateHabitTitle -> {
                val currentForm = state().creationForm
                if (currentForm is AddHabitForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(title = intent.value),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateHabitDeadline -> {
                val currentForm = state().creationForm
                if (currentForm is AddHabitForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(deadline = intent.deadline),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateHabitEmoji -> {
                val currentForm = state().creationForm
                if (currentForm is AddHabitForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(emoji = intent.emoji),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateHabitRepeatDays -> {
                val currentForm = state().creationForm
                if (currentForm is AddHabitForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(repeatDays = intent.days),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateTodoTitle -> {
                val currentForm = state().creationForm
                if (currentForm is AddTodoForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(title = intent.value),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateTodoDeadline -> {
                val currentForm = state().creationForm
                if (currentForm is AddTodoForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(deadline = intent.deadline),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateTodoStartDate -> {
                val currentForm = state().creationForm
                if (currentForm is AddTodoForm && currentForm.repeatPeriod is AddTodoForm.RepeatPeriod.Single) {
                    if (intent.startDate > currentForm.deadline) {
                        publish(AddHabitLabel.CreateTodoStartDateShouldBeEarlierError)
                    } else {
                        val newPeriod = AddTodoForm.RepeatPeriod.Single(intent.startDate)
                        dispatch(
                            AddHabitMessage.OpenNewCreationForm(
                                currentForm.copy(repeatPeriod = newPeriod),
                            ),
                        )
                    }
                }
            }

            is AddHabitIntent.UpdateTodoRepeatPeriod -> {
                val currentForm = state().creationForm
                if (currentForm is AddTodoForm) {
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(repeatPeriod = intent.repeatPeriod),
                        ),
                    )
                }
            }

            is AddHabitIntent.UpdateTodoIsRegular -> {
                val currentForm = state().creationForm
                if (currentForm is AddTodoForm) {
                    val newRepeatPeriod = if (currentForm.isRegular) {
                        AddTodoForm.RepeatPeriod.Single()
                    } else {
                        AddTodoForm.RepeatPeriod.Monthly()
                    }
                    dispatch(
                        AddHabitMessage.OpenNewCreationForm(
                            currentForm.copy(repeatPeriod = newRepeatPeriod),
                        ),
                    )
                }
            }

            is AddHabitIntent.CreateForm -> {
                scope.launch {
                    val currentForm = state().creationForm
                    if (currentForm is AddTodoForm && currentForm.repeatPeriod.isEmpty()) {
                        publish(AddHabitLabel.CreateTodoRepeatPeriodShouldBeNotEmptyError)
                    } else {
                        createItemUseCase.invoke(
                            creationForm = currentForm,
                            creationDay = Time.now(),
                        )
                            .onFailure {
                                publish(AddHabitLabel.ItemFailedToCreateCreated)
                            }
                            .onSuccess {
                                publish(AddHabitLabel.ItemSuccessfullyCreated)
                            }
                    }
                }
            }
        }
    }
}
