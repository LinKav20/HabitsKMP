package com.github.linkav20.habits.common.decompose

import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch

public inline fun <T : Any> bootAction(crossinline emitter: () -> T?): Bootstrapper<T> {
    return object : CoroutineBootstrapper<T>() {
        override fun invoke() {
            scope.launch {
                emitter()?.let { dispatch(it) }
            }
        }
    }
}
