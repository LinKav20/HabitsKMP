package com.github.linkav20.habits.debugmenu.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.module
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.github.linkav20.habits.debugmenu.api.DebugMenuContent
import com.github.linkav20.habits.debugmenu.impl.mvi.DebugMenuStore
import com.github.linkav20.habits.debugmenu.impl.mvi.DebugMenuStoreFactoryImpl
import com.github.linkav20.habits.debugmenu.impl.ui.DebugMenuContentImpl
import com.github.linkav20.habits.debugmenu.impl.ui.DefaultDebugMenuComponent

fun debugMenuFeatureModule() =
    module {
        factory<DebugMenuComponent.Factory> {
            DefaultDebugMenuComponent.Factory(get())
        }

        factory<DebugMenuContent> {
            DebugMenuContentImpl()
        }

        factory<DebugMenuStore.Factory> {
            DebugMenuStoreFactoryImpl(
                DefaultStoreFactory(),
            )
        }
    }
