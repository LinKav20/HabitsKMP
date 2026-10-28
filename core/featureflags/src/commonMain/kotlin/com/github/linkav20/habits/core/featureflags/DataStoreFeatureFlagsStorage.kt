package com.github.linkav20.habits.core.featureflags

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

public class DataStoreFeatureFlagsStorage(
    private val dataStore: DataStore<Preferences>,
) : FeatureFlagsStorage {
    override suspend fun get(flags: List<FeatureFlag>): Map<FeatureFlag, Boolean> {
        if (flags.isEmpty()) return emptyMap()

        val preferences = dataStore.data.first()
        return flags.associateWith { key ->
            preferences[key.preferenceKey()] ?: false
        }
    }

    override suspend fun setEnabled(key: FeatureFlag, enabled: Boolean) {
        dataStore.edit { preferences -> preferences[key.preferenceKey()] = enabled }
    }

    private fun FeatureFlag.preferenceKey(): Preferences.Key<Boolean> =
        booleanPreferencesKey("feature_flag_$key")
}
