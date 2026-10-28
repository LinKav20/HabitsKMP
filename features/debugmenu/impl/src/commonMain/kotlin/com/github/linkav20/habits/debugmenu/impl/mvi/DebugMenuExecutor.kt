package com.github.linkav20.habits.debugmenu.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.github.linkav20.habits.core.featureflags.usecase.GetFeatureFlagsUseCase
import com.github.linkav20.habits.core.featureflags.usecase.SetFeatureFlagUseCase
import com.github.linkav20.habits.core.utils.showNeedToRestartDialog
import com.github.linkav20.habits.datastore.datasource.SettingsStorage
import com.github.linkav20.habits.debugmenu.api.DebugMenuIntent
import com.github.linkav20.habits.debugmenu.api.DebugMenuLabel
import com.github.linkav20.habits.debugmenu.api.DebugMenuState
import kotlinx.coroutines.launch

internal class DebugMenuExecutor(
    private val setFeatureFlagUseCase: SetFeatureFlagUseCase,
    private val getFeatureFlagsUseCase: GetFeatureFlagsUseCase,
    private val settingsStorage: SettingsStorage,
    private val onShowNotification: () -> Unit,
) : CoroutineExecutor<DebugMenuIntent, DebugMenuAction, DebugMenuState, DebugMenuMessage, DebugMenuLabel>() {

    override fun executeAction(action: DebugMenuAction) {
        super.executeAction(action)
        when (action) {
            is DebugMenuAction.Init -> {
                scope.launch {
                    dispatch(DebugMenuMessage.FeatureFlagsLoaded(getFeatureFlagsUseCase.invoke()))
                    dispatch(
                        DebugMenuMessage.Data(
                            isStage = settingsStorage.getIsStage(),
                            prodToken = settingsStorage.getToken(isStage = false),
                            stageToken = settingsStorage.getToken(isStage = true),
                        ),
                    )
                }
            }
        }
    }

    override fun executeIntent(intent: DebugMenuIntent) {
        super.executeIntent(intent)
        when (intent) {
            is DebugMenuIntent.ChangeFeatureFlag -> {
                scope.launch {
                    setFeatureFlagUseCase.invoke(key = intent.key, enabled = intent.enabled)
                    dispatch(DebugMenuMessage.FeatureFlagChanged(intent.key, intent.enabled))
                }
            }

            is DebugMenuIntent.ChangeStage -> {
                scope.launch {
                    settingsStorage.setIsStage(isStage = intent.isStage)
                    dispatch(DebugMenuMessage.UpdateIsStage(isStage = intent.isStage))
                }.invokeOnCompletion {
                    scope.launch { showNeedToRestartDialog() }
                }
            }

            is DebugMenuIntent.ResetStageToken -> {
                scope.launch {
                    settingsStorage.clearStageToken()
                    dispatch(DebugMenuMessage.ClearStageToken)
                }
            }

            is DebugMenuIntent.ResetProdToken -> {
                scope.launch {
                    settingsStorage.clearProdToken()
                    dispatch(DebugMenuMessage.ClearProdToken)
                }
            }
        }
    }

    override fun dispose() {
        super.dispose()
        onShowNotification()
    }
}
