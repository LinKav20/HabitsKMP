package com.github.linkav20.habits

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import com.github.linkav20.habits.compose.di.initKoin
import com.github.linkav20.habits.core.utils.DeviceInfoFactory
import com.github.linkav20.habits.core.utils.Language
import com.github.linkav20.habits.core.utils.onSelectLanguage
import com.github.linkav20.habits.datastore.datasource.SettingsStorage

class MainApp : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()

        initKoin()

        DeviceInfoFactory.initialize(this.applicationContext)
        val settingsStorage: SettingsStorage by inject()
        appScope.launch {
            val language = settingsStorage.getLanguage().firstOrNull()
            onSelectLanguage(language ?: Language.System.iso)
        }
    }
}
