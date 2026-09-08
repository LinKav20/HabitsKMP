package com.github.linkav20.habits.core.utils

import kotlin.coroutines.cancellation.CancellationException

/**
 * Same contract as [runCatching], but never swallows coroutine cancellation.
 *
 * [runCatching] catches [Throwable], so a [CancellationException] thrown by a cancelled
 * coroutine turns into a `Result.failure` and the coroutine keeps running to completion.
 * Every suspending `runCatching` must use this function instead.
 */
public inline fun <T> runCatchingCancellable(block: () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Throwable) {
        Result.failure(exception)
    }
