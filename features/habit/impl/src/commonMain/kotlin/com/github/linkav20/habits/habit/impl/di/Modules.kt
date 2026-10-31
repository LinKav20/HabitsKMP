package com.github.linkav20.habits.habit.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.linkav20.habits.habit.api.detail.HabitComponent
import com.github.linkav20.habits.habit.api.detail.HabitContent
import com.github.linkav20.habits.habit.api.detail.HabitStore
import com.github.linkav20.habits.habit.impl.mvi.detail.HabitStoreFactoryImpl
import com.github.linkav20.habits.habit.impl.ui.detail.DefaultHabitComponent
import com.github.linkav20.habits.habit.impl.ui.detail.HabitContentImpl
import com.github.linkav20.habits.habit.api.HabitNavComponent
import com.github.linkav20.habits.habit.api.HabitNavContent
import com.github.linkav20.habits.habit.api.list.HabitListComponent
import com.github.linkav20.habits.habit.api.list.HabitListContent
import com.github.linkav20.habits.habit.api.list.HabitListStore
import com.github.linkav20.habits.habit.impl.ui.DefaultHabitNavComponent
import com.github.linkav20.habits.habit.impl.ui.HabitNavContentImpl
import com.github.linkav20.habits.habit.impl.ui.list.DefaultHabitListComponent
import com.github.linkav20.habits.habit.impl.ui.list.HabitListContentImpl
import com.github.linkav20.habits.habit.impl.mvi.list.HabitListStoreFactoryImpl
import com.github.linkav20.habits.habit.impl.data.HabitListRepositoryImpl
import com.github.linkav20.habits.habit.impl.domain.repository.HabitListRepository
import com.github.linkav20.habits.habit.impl.domain.usecase.CalculateHabitListUpdateUseCase
import com.github.linkav20.habits.habit.impl.domain.usecase.ObserveHabitsUseCase
import org.koin.dsl.module

fun createHabitFeatureModule() =
    module {
        factory<HabitNavComponent.Factory> { DefaultHabitNavComponent.Factory(get(), get()) }
        factory<HabitNavContent> { HabitNavContentImpl() }
        factory<HabitListComponent.Factory> { DefaultHabitListComponent.Factory(get()) }
        factory<HabitListContent> { HabitListContentImpl() }
        factory<HabitListStore.Factory> { HabitListStoreFactoryImpl(DefaultStoreFactory(), get(), get()) }
        factory<HabitListRepository> { HabitListRepositoryImpl(get()) }
        factory { ObserveHabitsUseCase(get()) }
        factory { CalculateHabitListUpdateUseCase() }

        factory<HabitComponent.Factory> {
            DefaultHabitComponent.Factory(get())
        }

        factory<HabitContent> {
            HabitContentImpl()
        }

        factory<HabitStore.Factory> {
            HabitStoreFactoryImpl(
                DefaultStoreFactory(),
            )
        }
    }
