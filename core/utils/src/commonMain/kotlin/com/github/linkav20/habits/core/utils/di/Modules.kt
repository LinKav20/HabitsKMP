package com.github.linkav20.habits.core.utils.di

import com.github.linkav20.habits.core.utils.AppVisibility
import org.koin.core.module.Module
import org.koin.dsl.module

public expect fun appLifecycleListenerProvider(): AppVisibility

public fun utilsModule(): Module =
    module {
        single { appLifecycleListenerProvider() }
    }
