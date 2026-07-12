package com.github.linkav20.habits.compose.usecase

import com.github.linkav20.habits.common.usecase.UnauthUseCase
import com.github.linkav20.habits.database.habits.dao.HabitsDao
import com.github.linkav20.habits.datastore.datasource.SettingsStorage
import com.github.linkav20.habits.network.GlobalAuthChannel

class UnauthUseCaseImpl(
    private val settingsStorage: SettingsStorage,
    private val globalAuthChannel: GlobalAuthChannel,
    private val courseDao: HabitsDao,
) : UnauthUseCase {
    override suspend fun invoke() {
        settingsStorage.setToken("")
        courseDao.clearAll()
        globalAuthChannel.notifyUnauthorized()
    }
}
