package com.github.linkav20.habits.core.uikit.components.snackbar

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class SnackBarNotifier {
    private val _messages = Channel<HabitSnackBarData>(Channel.Factory.BUFFERED)
    val messages: Flow<HabitSnackBarData> = _messages.receiveAsFlow()

    suspend fun showSnackBar(message: HabitSnackBarData) {
        _messages.send(message)
    }
}
