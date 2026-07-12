package com.github.linkav20.habits.compose.di

import com.github.linkav20.habits.compose.presentation.root.DefaultRootComponent
import com.github.linkav20.habits.compose.presentation.root.RootComponent
import com.github.linkav20.habits.compose.usecase.UnauthUseCaseImpl
import org.koin.dsl.module
import com.github.linkav20.habits.common.usecase.UnauthUseCase
import com.github.linkav20.habits.core.uikit.components.snackbar.SnackBarNotifier

val rootComponentsModule = module {
    factory<RootComponent.Factory> {
        DefaultRootComponent.Factory()
    }
    single { SnackBarNotifier() }

    factory<UnauthUseCase> {
        UnauthUseCaseImpl(get(), get(), get())
    }
}
