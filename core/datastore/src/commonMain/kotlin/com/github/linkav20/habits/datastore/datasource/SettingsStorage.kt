package com.github.linkav20.habits.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import com.github.linkav20.habits.core.utils.Environment
import com.github.linkav20.habits.core.utils.Language
import com.github.linkav20.habits.core.utils.currentEnvironment

class SettingsStorage(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val environment: Environment,
) {
    companion object {
        private val DEVICE_ID = stringPreferencesKey("device_id")
        private val TOKEN_STAGE = stringPreferencesKey("token_stage")
        private val TOKEN_PROD = stringPreferencesKey("token_prod")
        private val USER = stringPreferencesKey("user")
        private val THEME = stringPreferencesKey("theme")
        private val LANGUAGE = stringPreferencesKey("language")
        private val IS_STAGE = booleanPreferencesKey("is_stage")
    }

    private suspend fun setDeviceId(deviceId: String) {
        dataStore.edit { it[DEVICE_ID] = deviceId }
    }

    suspend fun getDeviceId(): String {
        return dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { it[DEVICE_ID] }
            .firstOrNull()
            ?.takeIf { it.isNotEmpty() }
            ?: currentEnvironment.deviceId.also { setDeviceId(it) }
    }

    suspend fun setIsStage(isStage: Boolean) {
        withContext(dispatcher) {
            dataStore.edit { it[IS_STAGE] = isStage }
        }
    }

    suspend fun getIsStage(): Boolean {
        return withContext(dispatcher) {
            dataStore.data
                .catch { emit(emptyPreferences()) }
                .map { preferences: Preferences -> preferences[IS_STAGE] }
                .firstOrNull()
                ?: environment.isDebug
        }
    }

    suspend fun setToken(token: String) =
        withContext(dispatcher) {
            val isStage = getIsStage()
            dataStore.edit { preferences: MutablePreferences ->
                if (isStage) {
                    preferences[TOKEN_STAGE] = token
                } else {
                    preferences[TOKEN_PROD] = token
                }
            }
        }

    suspend fun clearStageToken() {
        withContext(dispatcher) {
            dataStore.edit { preferences: MutablePreferences ->
                preferences[TOKEN_STAGE] = ""
            }
        }
    }

    suspend fun clearProdToken() {
        withContext(dispatcher) {
            dataStore.edit { preferences: MutablePreferences ->
                preferences[TOKEN_PROD] = ""
            }
        }
    }

    suspend fun getToken(
        defaultValue: String? = null,
        isStage: Boolean? = null,
    ): String? =
        withContext(dispatcher) {
            val localIsStage = isStage ?: getIsStage()
            dataStore.data
                .catch { emit(emptyPreferences()) }
                .map { preferences: Preferences ->
                    if (localIsStage) {
                        preferences[TOKEN_STAGE]
                    } else {
                        preferences[TOKEN_PROD]
                    }
                }
                .firstOrNull()
                ?: defaultValue
        }

    suspend fun setUser(user: UserStorage) =
        withContext(dispatcher) {
            val jsonString = Json.encodeToString(user)
            dataStore.edit { prefs -> prefs[USER] = jsonString }
        }

    val userFlow: Flow<UserStorage?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            prefs[USER]?.let { jsonString ->
                Json.decodeFromString<UserStorage>(jsonString)
            }
        }

    suspend fun setTheme(theme: ThemePreference) {
        withContext(dispatcher) {
            dataStore.edit { preferences: MutablePreferences ->
                preferences[THEME] = theme.name
            }
        }
    }

    suspend fun getTheme(): Flow<ThemePreference> {
        return withContext(dispatcher) {
            dataStore.data
                .catch { emit(emptyPreferences()) }
                .map { preferences: Preferences ->
                    preferences[THEME]?.let { theme ->
                        ThemePreference.valueOf(theme)
                    } ?: ThemePreference.SYSTEM
                }
        }
    }

    suspend fun setLanguage(language: String) {
        withContext(dispatcher) {
            dataStore.edit { preferences: MutablePreferences ->
                preferences[LANGUAGE] = language
            }
        }
    }

    suspend fun getLanguage(): Flow<String> {
        return withContext(dispatcher) {
            dataStore.data
                .catch { emit(emptyPreferences()) }
                .map { preferences: Preferences ->
                    preferences[LANGUAGE] ?: Language.System.iso
                }
        }
    }
}
