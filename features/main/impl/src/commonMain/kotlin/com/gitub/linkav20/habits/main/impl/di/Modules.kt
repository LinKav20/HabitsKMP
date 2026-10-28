package com.gitub.linkav20.habits.main.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.linkav20.habits.core.mvi.navigation.impl.NavStoreFactoryImpl
import com.gitub.linkav20.habits.main.api.MainNavComponent
import com.gitub.linkav20.habits.main.api.MainNavContent
import com.gitub.linkav20.habits.main.api.main.MainComponent
import com.gitub.linkav20.habits.main.api.main.MainContent
import com.gitub.linkav20.habits.main.api.main.MainStore
import com.gitub.linkav20.habits.main.impl.communication.GlobalCommunication
import com.gitub.linkav20.habits.main.impl.data.ItemsRepositoryImpl
import com.gitub.linkav20.habits.main.impl.data.MoodRepositoryImpl
import com.gitub.linkav20.habits.main.impl.domain.repository.ItemsRepository
import com.gitub.linkav20.habits.main.impl.domain.repository.MoodRepository
import com.gitub.linkav20.habits.main.impl.domain.usecase.GetItemsWithCompletionUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.GetMoodAndItemsUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.GetMoodUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetItemIsDoneUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.SetMoodUseCase
import com.gitub.linkav20.habits.main.impl.domain.usecase.UpdateTodoUseCase
import com.gitub.linkav20.habits.main.impl.mvi.main.MainStoreFactoryImpl
import com.gitub.linkav20.habits.main.impl.ui.DefaultMainNavComponent
import com.gitub.linkav20.habits.main.impl.ui.MainNavContentImpl
import com.gitub.linkav20.habits.main.impl.ui.main.DefaultMainComponent
import com.gitub.linkav20.habits.main.impl.ui.main.MainContentImpl
import org.koin.dsl.module

fun mainFeatureModule() =
    module {
        single { GlobalCommunication() }

        factory<MainComponent.Factory> {
            DefaultMainComponent.Factory(get())
        }

        factory<MainNavComponent.Factory> {
            DefaultMainNavComponent.Factory()
        }

        factory<MainContent> {
            MainContentImpl()
        }

        factory<MainNavContent> {
            MainNavContentImpl()
        }

        factory<MainStore.Factory> {
            MainStoreFactoryImpl(
                NavStoreFactoryImpl(DefaultStoreFactory()),
            )
        }

        factory<ItemsRepository> { ItemsRepositoryImpl(get(), get(), get(), get()) }

        factory<MoodRepository> { MoodRepositoryImpl(get()) }

        factory { GetItemsWithCompletionUseCase(get()) }
        factory { GetMoodUseCase(get()) }
        factory { GetMoodAndItemsUseCase(get(), get()) }
        factory { SetItemIsDoneUseCase(get()) }
        factory { SetMoodUseCase(get()) }
        factory { UpdateTodoUseCase(get()) }
    }
