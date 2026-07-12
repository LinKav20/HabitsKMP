package com.github.linkav20.habits.auth.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.module
import com.github.linkav20.habits.auth.api.AuthComponent
import com.github.linkav20.habits.auth.api.AuthContent
import com.github.linkav20.habits.auth.api.AuthStore
import com.github.linkav20.habits.auth.impl.data.datasource.AuthRepositoryImpl
import com.github.linkav20.habits.auth.impl.domain.repository.AuthRepository
import com.github.linkav20.habits.auth.impl.mvi.AuthStoreFactoryImpl
import com.github.linkav20.habits.auth.impl.ui.AuthContentImpl
import com.github.linkav20.habits.auth.impl.ui.DefaultAuthComponent
import com.github.linkav20.habits.core.mvi.navigation.impl.NavStoreFactoryImpl

fun authFeatureModule() =
    module {
        factory<AuthComponent.Factory> {
            DefaultAuthComponent.Factory(get())
        }

        factory<AuthContent> {
            AuthContentImpl()
        }

        factory<AuthStore.Factory> {
            AuthStoreFactoryImpl(
                NavStoreFactoryImpl(DefaultStoreFactory()),
            )
        }

        single<AuthRepository> { AuthRepositoryImpl() }
    }
