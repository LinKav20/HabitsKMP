package com.github.linkav20.habits.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.cancellation.CancellationException

public inline fun <T> Result<T>.onFailureIgnoreCancellation(action: (exception: Throwable) -> Unit): Result<T> {
    exceptionOrNull()
        ?.takeIf { it !is CancellationException }
        ?.let { action(it) }
    return this
}

public fun <T, R> StateFlow<T>.mapStateFlow(
    scope: CoroutineScope,
    flowTransform: Flow<T>.() -> Flow<T> = { this },
    transform: (value: T) -> R,
): StateFlow<R> {
    return this
        .drop(1) // Since we have initialValue in stateIn
        .flowTransform()
        .map { transform(it) }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = transform(this.value),
        )
}
