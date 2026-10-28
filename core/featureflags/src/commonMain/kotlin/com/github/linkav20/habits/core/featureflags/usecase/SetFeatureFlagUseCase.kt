package com.github.linkav20.habits.core.featureflags.usecase

import com.github.linkav20.habits.core.featureflags.FeatureFlag
import com.github.linkav20.habits.core.featureflags.FeatureFlagsStorage

public class SetFeatureFlagUseCase(
    private val featureFlagsStorage: FeatureFlagsStorage,
) {
    public suspend fun invoke(key: FeatureFlag, enabled: Boolean) {
        featureFlagsStorage.setEnabled(key, enabled)
    }
}
