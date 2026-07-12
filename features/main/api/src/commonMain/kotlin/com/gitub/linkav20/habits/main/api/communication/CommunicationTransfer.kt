package com.gitub.linkav20.habits.main.api.communication

interface CommunicationTransfer<T : Any> {

    suspend fun sendEvent(event: T)
}
