package com.github.linkav20.habits.network

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class GlobalAuthChannel {
    private val _unauthorizedEvents = Channel<Unit>(Channel.CONFLATED)
    val unauthorizedEvents = _unauthorizedEvents.receiveAsFlow()

    suspend fun notifyUnauthorized() {
        _unauthorizedEvents.send(Unit)
    }
}
