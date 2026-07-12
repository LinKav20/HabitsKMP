package com.gitub.linkav20.habits.main.impl.communication

import com.gitub.linkav20.habits.main.api.communication.GlobalCommunicationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GlobalCommunication {
    private val _events = MutableSharedFlow<GlobalCommunicationEvent>(
        extraBufferCapacity = Channel.UNLIMITED,
    )
    val events: SharedFlow<GlobalCommunicationEvent> = _events.asSharedFlow()

    suspend fun sendEvent(event: GlobalCommunicationEvent) {
        _events.emit(event)
    }
}
