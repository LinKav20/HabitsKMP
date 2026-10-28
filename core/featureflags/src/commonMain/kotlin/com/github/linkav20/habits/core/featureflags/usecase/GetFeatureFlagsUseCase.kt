package com.github.linkav20.habits.core.featureflags.usecase

import com.github.linkav20.habits.core.featureflags.FeatureFlag
import com.github.linkav20.habits.core.featureflags.FeatureFlagsStorage

public class GetFeatureFlagsUseCase(
    private val featureFlagsStorage: FeatureFlagsStorage,
) {
    public suspend fun invoke(flags: List<FeatureFlag> = FeatureFlag.entries): Map<FeatureFlag, Boolean> =
        featureFlagsStorage.get(flags)
}
