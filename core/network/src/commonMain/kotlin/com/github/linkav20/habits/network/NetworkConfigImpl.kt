package com.github.linkav20.habits.network

import kotlinx.coroutines.runBlocking
import com.github.linkav20.habits.datastore.datasource.SettingsStorage

private const val STAGE_URL = "https://stage-example.ru/"
private const val PROD_URL = "https://example.ru/"
private const val DOWNLOAD_URL = "https://example.ru/"

class NetworkConfigImpl(private val settingsStorage: SettingsStorage) : NetworkConfig {
    private val _isStage: Boolean = runBlocking { settingsStorage.getIsStage() }
    override val isStage: Boolean get() = _isStage

    override val baseUrl: String
        get() = if (isStage) STAGE_URL else PROD_URL


    override fun downloadUrl(fileName: String): String {
        return "$DOWNLOAD_URL$fileName"
    }
}
