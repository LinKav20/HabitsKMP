package com.github.linkav20.habits.habit.impl.mvi.list

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.linkav20.habits.habit.api.list.HabitListIntent
import com.github.linkav20.habits.habit.api.list.HabitListStore
import com.github.linkav20.habits.habit.impl.domain.model.HabitListUpdate
import com.github.linkav20.habits.habit.impl.domain.usecase.CalculateHabitListUpdateUseCase
import com.github.linkav20.habits.habit.impl.domain.usecase.ObserveHabitsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

internal class HabitListExecutor(
    private val observeHabits: ObserveHabitsUseCase,
    private val calculateHabitListUpdate: CalculateHabitListUpdateUseCase,
) :
    CoroutineExecutor<HabitListIntent, HabitListAction, HabitListStore.State, HabitListMessage, Nothing>() {
    private var observation: Job? = null

    override fun executeAction(action: HabitListAction) {
        when (action) {
            HabitListAction.Init -> observe()
        }
    }

    override fun executeIntent(intent: HabitListIntent) {
        when (intent) {
            HabitListIntent.Retry -> observe()
        }
    }

    private fun observe() {
        observation?.cancel()
        dispatch(HabitListMessage.Loading)
        observation = scope.launch {
            observeHabits()
                .mapNotNull { items ->
                    calculateHabitListUpdate(
                        previous = (state() as? HabitListStore.State.Content)?.items,
                        current = items,
                    )
                }
                .catch { dispatch(HabitListMessage.Failed) }
                .collect { update ->
                    when (update) {
                        is HabitListUpdate.Initial -> dispatch(HabitListMessage.Loaded(update.items))
                        is HabitListUpdate.Changed -> dispatch(HabitListMessage.ItemsChanged(update.items))
                    }
                }
        }
    }
}
