package com.gitub.linkav20.habits.main.api.communication

sealed interface GlobalCommunicationEvent {
    data object DataUpdated : GlobalCommunicationEvent
}
