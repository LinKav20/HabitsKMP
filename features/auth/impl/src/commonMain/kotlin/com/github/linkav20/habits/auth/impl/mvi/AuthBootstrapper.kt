package com.github.linkav20.habits.auth.impl.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class AuthBootstrapper : CoroutineBootstrapper<AuthAction>() {

    override fun invoke() {
        println("Bootstrapper inited")
    }
}
