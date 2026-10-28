package com.github.linkav20.habits.core.featureflags

public interface FeatureFlagsStorage {
    public suspend fun get(flags: List<FeatureFlag>): Map<FeatureFlag, Boolean>

    public suspend fun setEnabled(key: FeatureFlag, enabled: Boolean)
}
