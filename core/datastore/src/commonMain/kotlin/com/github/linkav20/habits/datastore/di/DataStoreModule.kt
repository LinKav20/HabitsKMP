package com.github.linkav20.habits.datastore.di

import com.github.linkav20.habits.datastore.createDataStore
import com.github.linkav20.habits.datastore.dataStorePath
import com.github.linkav20.habits.datastore.datasource.SettingsStorage
import org.koin.dsl.module
import com.github.linkav20.habits.core.utils.currentEnvironment

val dataStoreModule = module {
    single { createDataStore { dataStorePath() } }
    factory { SettingsStorage(get(), environment = currentEnvironment) }
}
