package com.github.linkav20.habit.createitem.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.github.linkav20.habits.core.uikit.components.snackbar.SnackBarNotifier
import com.github.linkav20.habits.core.uikit.components.snackbar.HabitSnackBarData
import com.github.linkav20.habits.core.uikit.components.snackbar.HabitSnackBarType
import com.github.linkav20.habits.createitem.api.AddHabitComponent
import com.github.linkav20.habits.createitem.api.AddHabitEvent
import com.github.linkav20.habits.createitem.api.AddHabitIntent
import com.github.linkav20.habits.createitem.api.AddHabitLabel
import com.github.linkav20.habits.createitem.api.AddHabitStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultAddHabitComponent private constructor(
    componentContext: ComponentContext,
    private val storeFactory: AddHabitStore.Factory,
    private val snackBarNotifier: SnackBarNotifier,
    private val onBackClick: () -> Unit,
) : AddHabitComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    override val model: StateFlow<AddHabitStore.State> = store.stateFlow
    private val scope = coroutineScope()

    init {
        store.labels
            .onEach { label ->
                when (label) {
                    AddHabitLabel.ItemSuccessfullyCreated -> {
                        snackBarNotifier.showSnackBar(
                            message = HabitSnackBarData(
                                deduplicateId = "Create",
                                titleText = "Success",
                                type = HabitSnackBarType.SUCCESS,
                            ),
                        )
                        onBackClick()
                    }

                    AddHabitLabel.ItemFailedToCreateCreated -> {
                        snackBarNotifier.showSnackBar(
                            message = HabitSnackBarData(
                                deduplicateId = "Failed",
                                titleText = "Failed",
                                type = HabitSnackBarType.ERROR,
                            ),
                        )
                    }

                    AddHabitLabel.CreateTodoStartDateShouldBeEarlierError -> {
                        snackBarNotifier.showSnackBar(
                            message = HabitSnackBarData(
                                deduplicateId = "CreateTodoStartDateShouldBeEarlierError",
                                titleText = "Start date should be earlier than deadline or like deadline",
                                type = HabitSnackBarType.ERROR,
                            ),
                        )
                    }

                    AddHabitLabel.CreateTodoRepeatPeriodShouldBeNotEmptyError -> {
                        snackBarNotifier.showSnackBar(
                            message = HabitSnackBarData(
                                deduplicateId = "CreateTodoRepeatPeriodShouldBeNotEmptyError",
                                titleText = "Repeat todo period should be entered. If you do not want to repeat todo turn off regular toogle",
                                type = HabitSnackBarType.ERROR,
                            ),
                        )
                    }
                }
            }.launchIn(scope)
    }

    override fun handleEvent(event: AddHabitEvent) {
        when (event) {
            is AddHabitEvent.OnBackCLick -> onBackClick()
            is AddHabitEvent.OpenNewCreationForm -> {
                store.accept(AddHabitIntent.OpenNewCreationForm(event.creationForm))
            }

            is AddHabitEvent.UpdateHabitTitle -> {
                store.accept(AddHabitIntent.UpdateHabitTitle(event.value))
            }

            is AddHabitEvent.UpdateHabitRepeatDays -> {
                store.accept(AddHabitIntent.UpdateHabitRepeatDays(event.days))
            }

            is AddHabitEvent.UpdateHabitDeadline -> {
                store.accept(AddHabitIntent.UpdateHabitDeadline(event.deadline))
            }

            is AddHabitEvent.UpdateTodoTitle -> {
                store.accept(AddHabitIntent.UpdateTodoTitle(event.value))
            }

            is AddHabitEvent.UpdateTodoIsRegular -> {
                store.accept(AddHabitIntent.UpdateTodoIsRegular)
            }

            is AddHabitEvent.UpdateTodoStartDate -> {
                store.accept(AddHabitIntent.UpdateTodoStartDate(event.startDate))
            }

            is AddHabitEvent.UpdateTodoDeadline -> {
                store.accept(AddHabitIntent.UpdateTodoDeadline(event.deadline))
            }

            is AddHabitEvent.UpdateTodoRepeatPeriod -> {
                store.accept(AddHabitIntent.UpdateTodoRepeatPeriod(event.repeatPeriod))
            }


            is AddHabitEvent.CreateForm -> {
                store.accept(AddHabitIntent.CreateForm)
            }

            is AddHabitEvent.UpdateHabitEmoji -> {
                store.accept(AddHabitIntent.UpdateHabitEmoji(event.emoji))
            }
        }
    }

    internal class Factory(
        private val storeFactory: AddHabitStore.Factory,
    ) : AddHabitComponent.Factory,
        KoinComponent {
        override fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): AddHabitComponent {
            return DefaultAddHabitComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                onBackClick = onBackClick,
                snackBarNotifier = get(),
            )
        }
    }
}
