package com.gitub.linkav20.habits.main.impl.mvi.main

import kotlinx.coroutines.channels.SendChannel
import com.github.linkav20.habits.core.mvi.navigation.impl.NavigationExecutor
import com.github.linkav20.habits.core.utils.AppVisibility
import com.github.linkav20.habits.core.utils.Time
import com.github.linkav20.habits.database.habits.model.todo.TodoRepeatRule
import com.gitub.linkav20.habits.main.api.main.MainIntent
import com.gitub.linkav20.habits.main.api.main.MainNav
import com.gitub.linkav20.habits.main.api.main.MainStore
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItem
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.MainData
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.impl.domain.usecase.GetMoodAndItemsUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetItemIsDoneUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetMoodUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.UpdateTodoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

internal class MainExecutor(
    channel: SendChannel<MainNav>,
    private val getMoodAndItemsUseCase: GetMoodAndItemsUseCase,
    private val setItemDoneUseCase: SetItemIsDoneUseCase,
    private val setMoodUseCase: SetMoodUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val appVisibility: AppVisibility,
) :
    NavigationExecutor<MainIntent, MainAction, MainStore.State, MainMessage, Any, MainNav>(
        channel,
    ) {

    private var observeCurrentDateJob: Job? = null
    private val appVisibilityListener: AppVisibility.Listener = object : AppVisibility.Listener {
        override fun onAppGoesBackground() {
            stopObserveCurrentDate()
        }

        override fun onAppGoesForeground() {
            observeCurrentDate()
        }
    }

    override fun executeAction(action: MainAction) {
        super.executeAction(action)
        when (action) {
            MainAction.Init -> {
                appVisibility.registerListener(appVisibilityListener)
                scope.launch {
                    loadData(today = Time.now())
                }.invokeOnCompletion {
                    observeCurrentDate(forceUpdate = true)
                }
            }
        }
    }

    override fun executeIntent(intent: MainIntent) {
        super.executeIntent(intent)
        when (intent) {
            is MainIntent.ChangeSelectedDate -> {
                dispatch(
                    MainMessage.ChangeSelectedDate(
                        selectedDate = intent.date,
                    ),
                )
            }

            is MainIntent.ChangeItemIsDone -> {
                val state = state()
                val data = state.data ?: return

                val updatedData = data.copy(
                    items = data.items.map { itemWithCompletion ->
                        if (itemWithCompletion.checkingItem == intent.item) {
                            val updatedCompletion = itemWithCompletion.completion.toMutableMap().apply {
                                val value = changeItemIsDone(
                                    item = itemWithCompletion.checkingItem,
                                    isDone = this[intent.date],
                                    date = intent.date,
                                )
                                if (value == null) {
                                    remove(intent.date)
                                } else {
                                    this[intent.date] = value
                                }
                            }
                            itemWithCompletion.copy(completion = updatedCompletion)
                        } else {
                            itemWithCompletion
                        }
                    },
                )

                dispatch(
                    MainMessage.UpdateCompletion(
                        data = updatedData,
                    ),
                )
            }

            is MainIntent.SetRate -> {
                val state = state()
                val data = state.data ?: return
                updateMoodInDatabase(
                    rate = intent.rate,
                    date = intent.date,
                )
                val updatedData = data.copy(
                    moods = data.moods + (intent.date to intent.rate),
                )
                dispatch(
                    MainMessage.UpdateMood(
                        data = updatedData,
                    ),
                )
            }

            is MainIntent.UpdateData -> {
                scope.launch {
                    val today = state().today
                    val weeks = Time.getWeekRange(
                        day = today,
                        weeksAfter = END,
                        weeksBefore = START,
                    )
                    getMoodAndItemsUseCase.invoke(
                        startDate = weeks.first,
                        endDate = weeks.second,
                    ).onSuccess { data ->
                        dispatch(
                            MainMessage.DataUpdated(
                                data = data,
                            ),
                        )
                    }.onFailure {
                        // todo
                    }
                }
            }
        }
    }

    private fun stopObserveCurrentDate() {
        observeCurrentDateJob?.cancel()
        observeCurrentDateJob = null
    }

    private fun observeCurrentDate(forceUpdate: Boolean = false) {
        stopObserveCurrentDate()
        observeCurrentDateJob = scope.launch {
            while (true) {
                delay(1000)
                val state = state()
                val today = Time.now()
                val selectedDate = if (state.today == state.selectedDate) {
                    today
                } else {
                    state.selectedDate
                }
                if (state.today != today || forceUpdate) {
                    val data = failAllUndoneTasks() ?: continue
                    val days = Time.getDateRangeInclusive(data.startDate, data.endDate)
                    val currentPage = days.indexOf(selectedDate) / DAYS_IN_WEEK
                    dispatch(
                        MainMessage.LoadInitialData(
                            today = today,
                            selectedDate = selectedDate,
                            currentPage = currentPage,
                            data = data,
                        ),
                    )
                }
            }
        }
    }

    private fun failAllUndoneTasks(): MainData? {
        val state = state()
        val data = state.data ?: return null
        val today = Time.now()

        val updatedItems = data.items.map { itemWithCompletion ->
            val item = itemWithCompletion.checkingItem
            if (item is TodoItem && item.repeatPeriod is TodoItem.RepeatPeriod.Single) {
                // todo do not reschedule if done
                val (item, isDone) = rescheduleItem(item)
                val updatedCompletion = if (isDone == null) {
                    emptyMap()
                } else {
                    itemWithCompletion.completion.keys.associateWith { isDone }
                }
                itemWithCompletion.copy(
                    checkingItem = item,
                    completion = updatedCompletion,
                )
            } else {
                val updatedCompletion = itemWithCompletion.completion.toMutableMap()
                Time.getDateRangeInclusive(data.startDate, data.endDate)
                    .filter { date -> date < today && item.isPlanned(date) }
                    .forEach { date ->
                        val value = changeItemCompletion(
                            item = item,
                            isDone = updatedCompletion[date],
                            date = date,
                        )
                        if (value == null) {
                            updatedCompletion.remove(date)
                        } else {
                            updatedCompletion[date] = value
                        }
                    }
                itemWithCompletion.copy(completion = updatedCompletion)
            }
        }

        return data.copy(
            items = updatedItems,
        )
    }


    private suspend fun loadData(
        today: LocalDate,
    ) {
        val weeks = Time.getWeekRange(
            day = today,
            weeksAfter = END,
            weeksBefore = START,
        )
        getMoodAndItemsUseCase.invoke(
            startDate = weeks.first,
            endDate = weeks.second,
        ).onSuccess { data ->
            dispatch(
                MainMessage.LoadInitialData(
                    today = today,
                    selectedDate = today,
                    data = data,
                    currentPage = START,
                ),
            )
        }.onFailure {
            // todo
        }
    }

    private fun changeItemIsDone(
        item: CheckingItem,
        isDone: Boolean?,
        date: LocalDate,
    ): Boolean? {
        val isDone = if (isDone == null || !isDone) {
            true
        } else if (
            date < Time.now() &&
            (item as? HabitItem)?.daysForHabit?.contains(date.dayOfWeek) == true
        ) {
            false
        } else {
            null
        }
        scope.launch {
            setItemDoneUseCase.invoke(
                item = item,
                date = date,
                isDone = isDone,
            )
        }
        return isDone
    }

    private fun changeItemCompletion(
        item: CheckingItem,
        isDone: Boolean?,
        date: LocalDate,
    ): Boolean? {
        val isItemDone = when (item) {
            is HabitItem -> {
                if (item.daysForHabit.contains(date.dayOfWeek) && isDone == null) {
                    false
                } else {
                    isDone
                }
            }

            is TodoItem -> {
                when (item.repeatPeriod) {
                    is TodoItem.RepeatPeriod.Single -> {
                        null
                    }

                    else -> {
                        if (item.isPlanned(date) && isDone == null) {
                            false
                        } else {
                            isDone
                        }
                    }
                }
            }

            else -> null
        }
        scope.launch {
            setItemDoneUseCase.invoke(
                item = item,
                date = date,
                isDone = isItemDone,
            )
        }
        return isItemDone
    }

    private fun CheckingItem.isPlanned(date: LocalDate): Boolean = when (this) {
        is HabitItem -> dateInHabitRange(date) && date.dayOfWeek in daysForHabit
        is TodoItem -> isPlanned(date)
        else -> false
    }

    override fun dispose() {
        super.dispose()
        stopObserveCurrentDate()
        appVisibility.unregisterListener(appVisibilityListener)
    }

    private fun updateMoodInDatabase(
        rate: Int,
        date: LocalDate,
    ) {
        scope.launch {
            setMoodUseCase.invoke(
                rate = rate,
                date = date,
            ).onSuccess {
                // todo
            }.onFailure {
                // todo
            }
        }
    }

    private fun rescheduleItem(item: TodoItem): Pair<TodoItem, Boolean?> {
        return if (item.shouldReschedule() == true) {
            val newDate = Time.now()
            scope.launch {
                val newItem = item.copy(
                    repeatPeriod = (item.repeatPeriod as TodoItem.RepeatPeriod.Single).copy(startDate = newDate),
                )
                updateTodoUseCase.invoke(newItem)
            }
            item.copy(repeatPeriod = TodoItem.RepeatPeriod.Single(newDate)) to null
        } else if (item.shouldFail() == true) {
            val newDate = item.endDate
            if (newDate == null) {
                item to null
            } else {
                scope.launch {
                    val newItem = item.copy(
                        repeatPeriod = (item.repeatPeriod as TodoItem.RepeatPeriod.Single).copy(startDate = newDate),
                    )
                    updateTodoUseCase.invoke(newItem)
                }
                item.copy(repeatPeriod = TodoItem.RepeatPeriod.Single(newDate)) to false
            }
        } else {
            item to null
        }
    }

    companion object {
        private const val DAYS_IN_WEEK = 7
        private const val END = 2
        private const val START = 4
    }
}
