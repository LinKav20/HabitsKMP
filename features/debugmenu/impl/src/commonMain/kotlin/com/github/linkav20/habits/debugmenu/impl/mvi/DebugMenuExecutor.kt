package com.github.linkav20.habits.debugmenu.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import com.github.linkav20.habits.core.utils.showNeedToRestartDialog
import com.github.linkav20.habits.datastore.datasource.SettingsStorage
import com.github.linkav20.habits.debugmenu.api.DebugMenuIntent
import com.github.linkav20.habits.debugmenu.api.DebugMenuLabel
import com.github.linkav20.habits.debugmenu.api.DebugMenuState

internal class DebugMenuExecutor(
    private val settingsStorage: SettingsStorage,
    private val onShowNotification: () -> Unit,
) : CoroutineExecutor<DebugMenuIntent, DebugMenuAction, DebugMenuState, DebugMenuMessage, DebugMenuLabel>() {

    override fun executeAction(action: DebugMenuAction) {
        super.executeAction(action)
        when (action) {
            is DebugMenuAction.Init -> {
                scope.launch {
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
