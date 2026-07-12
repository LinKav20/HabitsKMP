package com.github.linkav20.habits.habit.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.linkav20.habits.habit.api.HabitComponent
import com.github.linkav20.habits.habit.api.HabitContent
import com.github.linkav20.habits.habit.api.HabitStore
import com.github.linkav20.habits.habit.impl.mvi.HabitStoreFactoryImpl
import com.github.linkav20.habits.habit.impl.ui.DefaultHabitComponent
import com.github.linkav20.habits.habit.impl.ui.HabitContentImpl
import org.koin.dsl.module

fun createHabitFeatureModule() =
    module {
        factory<HabitComponent.Factory> {
            com.github.linkav20.habits.habit.impl.ui.DefaultHabitComponent.Factory(get())
        }

        factory<HabitContent> {
            HabitContentImpl()
        }

        factory<HabitStore.Factory> {
            com.github.linkav20.habits.habit.impl.mvi.HabitStoreFactoryImpl(
                DefaultStoreFactory(),
            )
        }
    }
