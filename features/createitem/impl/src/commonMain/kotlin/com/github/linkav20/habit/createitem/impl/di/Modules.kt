package com.github.linkav20.habit.createitem.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.linkav20.habit.createitem.impl.data.CreationItemRepositoryImpl
import com.github.linkav20.habit.createitem.impl.domain.repository.CreationItemRepository
import com.github.linkav20.habit.createitem.impl.domain.usecase.CreateItemUseCase
import com.github.linkav20.habit.createitem.impl.mvi.AddHabitStoreFactoryImpl
import com.github.linkav20.habit.createitem.impl.ui.AddHabitContentImpl
import com.github.linkav20.habit.createitem.impl.ui.DefaultAddHabitComponent
import com.github.linkav20.habits.createitem.api.AddHabitComponent
import com.github.linkav20.habits.createitem.api.AddHabitContent
import com.github.linkav20.habits.createitem.api.AddHabitStore
import org.koin.dsl.module

fun createItemFeatureModule() =
    module {
        factory<AddHabitComponent.Factory> {
            DefaultAddHabitComponent.Factory(get())
        }

        factory<AddHabitContent> {
            AddHabitContentImpl()
        }

        factory<AddHabitStore.Factory> {
            AddHabitStoreFactoryImpl(
                DefaultStoreFactory(),
            )
        }

        factory<CreationItemRepository> {
            CreationItemRepositoryImpl(
                get(),
                get(),
            )
        }

        factory { CreateItemUseCase(get()) }
    }
