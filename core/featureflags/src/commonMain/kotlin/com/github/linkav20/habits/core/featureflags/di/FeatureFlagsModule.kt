package com.github.linkav20.habits.core.featureflags.di

import com.github.linkav20.habits.core.featureflags.DataStoreFeatureFlagsStorage
import com.github.linkav20.habits.core.featureflags.FeatureFlagsStorage
import com.github.linkav20.habits.core.featureflags.usecase.GetFeatureFlagsUseCase
import com.github.linkav20.habits.core.featureflags.usecase.SetFeatureFlagUseCase
import org.koin.dsl.module

public val featureFlagsModule = module {
    single<FeatureFlagsStorage> { DataStoreFeatureFlagsStorage(get()) }
    factory { GetFeatureFlagsUseCase(get()) }
    factory { SetFeatureFlagUseCase(get()) }
}
