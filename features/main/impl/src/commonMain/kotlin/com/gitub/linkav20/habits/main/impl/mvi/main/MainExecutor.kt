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
import com.gitub.linkav20.habits.main.api.domain.model.main.CheckingItemWithCompletion
import com.gitub.linkav20.habits.main.api.domain.model.main.HabitItem
import com.gitub.linkav20.habits.main.api.domain.model.main.ItemsWithMood
import com.gitub.linkav20.habits.main.api.domain.model.main.TodoItem
import com.gitub.linkav20.habits.main.impl.domain.usecase.GetMoodAndItemsUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetItemIsDoneUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetMoodUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.UpdateTodoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

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
                        tasks = getTasks(
                            data = state().itemsWithMood.checkingItemWithCompletion,
                            selectedDate = intent.date,
                        ),
                    ),
                )
            }

            is MainIntent.ChangeItemIsDone -> {
                val state = state()

                val updatedHabitsWithMood = state.itemsWithMood.copy(
                    checkingItemWithCompletion = state.itemsWithMood.checkingItemWithCompletion.map { itemWithCompletion ->
                        if (itemWithCompletion.checkingItem == intent.item) {
                            val updatedCompletion = itemWithCompletion.completion.map { (date, isDone) ->
                                if (date == intent.date) {
                                    val newItem = changeItemIsDone(
                                        item = itemWithCompletion.checkingItem,
                                        isDone = isDone,
                                        date = date,
                                    )
                                    newItem
                                } else {
                                    date to isDone
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
                        itemsWithMood = updatedHabitsWithMood,
                        tasks = getTasks(
                            selectedDate = state.selectedDate,
                            data = updatedHabitsWithMood.checkingItemWithCompletion,
                        ),
                    ),
                )
            }

            is MainIntent.SetRate -> {
                val state = state()
                val updatedHabitsWithMood = state.itemsWithMood.copy(
                    mood = state.itemsWithMood.mood.map { mood ->
                        if (mood.date == intent.date) {
                            updateMoodInDatabase(
                                rate = intent.rate,
                                date = intent.date,
                            )
                            mood.copy(rate = intent.rate)
                        } else {
                            mood
                        }
                    },
                )
                dispatch(
                    MainMessage.UpdateMood(
                        itemsWithMood = updatedHabitsWithMood,
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
                        pagesCount = END + START,
                    ).onSuccess { data ->
                        dispatch(
                            MainMessage.DataUpdated(
                                itemsWithMood = data,
                                tasks = getTasks(
                                    data = data.checkingItemWithCompletion,
                                    selectedDate = today,
                                ),
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
                    val itemsWithMood = failAllUndoneTasks()
                    val currentPage = itemsWithMood.days.indexOf(selectedDate) / DAYS_IN_WEEK
                    dispatch(
                        MainMessage.LoadInitialData(
                            today = today,
                            selectedDate = selectedDate,
                            currentPage = currentPage,
                            itemsWithMood = itemsWithMood.copy(
                                pagesCount = itemsWithMood.days.size / DAYS_IN_WEEK,
                            ),
                            tasks = getTasks(
                                selectedDate = selectedDate,
                                data = itemsWithMood.checkingItemWithCompletion,
                            ),
                        ),
                    )
                }
            }
        }
    }

    private fun failAllUndoneTasks(): ItemsWithMood {
        val state = state()
        val today = Time.now()

        val updatedItems = state.itemsWithMood.checkingItemWithCompletion.map { itemWithCompletion ->
            val item = itemWithCompletion.checkingItem
            if (item is TodoItem && item.repeatPeriod is TodoItem.RepeatPeriod.Single) {
                // todo do not reschedule if done
                val (item, isDone) = rescheduleItem(item)
                val updatedCompletion = itemWithCompletion.completion.map { (date, _) -> date to isDone }
                itemWithCompletion.copy(
                    checkingItem = item,
                    completion = updatedCompletion,
                )
            } else {
                val updatedCompletion = itemWithCompletion.completion.map { (date, isDone) ->
                    if (date < today) {
                        changeItemCompletion(
                            item = item,
                            isDone = isDone,
                            date = date,
                        )
                    } else {
                        date to isDone
                    }
                }
                itemWithCompletion.copy(completion = updatedCompletion)
            }
        }

        return state.itemsWithMood.copy(
            checkingItemWithCompletion = updatedItems,
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
            pagesCount = END + START,
        ).onSuccess { data ->
            dispatch(
                MainMessage.LoadInitialData(
                    today = today,
                    selectedDate = today,
                    itemsWithMood = data,
                    currentPage = START,
                    tasks = getTasks(
                        data = data.checkingItemWithCompletion,
                        selectedDate = today,
                    ),
                ),
            )
        }.onFailure {
            // todo
        }
    }

    private fun getTasks(
        selectedDate: LocalDate,
        data: List<CheckingItemWithCompletion>,
    ): Map<LocalDate, List<Pair<CheckingItem, Boolean>>> {
        val datesForTasksList = getListDatesForTasksListBySelectedDay(selectedDate)
        val completionMap = data.associate { itemWithCompletion ->
            itemWithCompletion.checkingItem to itemWithCompletion.completion.toMap()
        }
        val tasksWithDates = data.flatMap { checkingItemWithCompletion ->
            val item = checkingItemWithCompletion.checkingItem
            val completionByDate = completionMap[item] ?: emptyMap()
            when (item) {
                is HabitItem -> datesForTasksList
                    .filter {
                        datesForTasksList.any { date -> date >= item.startDate }
                                && it.dayOfWeek in item.daysForHabit
                    }
                    .map { date ->
                        val isDone = completionByDate[date] ?: false
                        date to (item to isDone)
                    }

                is TodoItem -> datesForTasksList
                    .filter { item.isPlanned(it) }
                    .map { date ->
                        val isDone = completionByDate[date] ?: false
                        date to (item to isDone)
                    }

                else -> emptyList()
            }
        }
        val groupedTasks = tasksWithDates.groupBy(
            keySelector = { (date, _) -> date },
            valueTransform = { (_, pair) -> pair },
        )
        return groupedTasks
    }

    private fun getListDatesForTasksListBySelectedDay(selectedDate: LocalDate): List<LocalDate> {
        val today = Time.now()
        return if (selectedDate == today) {
            listOf(
                today,
                today.plus(1, DAY),
                today.plus(2, DAY),
                today.plus(3, DAY),
                today.plus(4, DAY),
            )
        } else {
            listOf(
                selectedDate,
            )
        }
    }

    private fun changeItemIsDone(
        item: CheckingItem,
        isDone: Boolean?,
        date: LocalDate,
    ): Pair<LocalDate, Boolean?> {
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
        return date to isDone
    }

    private fun changeItemCompletion(
        item: CheckingItem,
        isDone: Boolean?,
        date: LocalDate,
    ): Pair<LocalDate, Boolean?> {
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
        return date to isItemDone
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
