package com.gitub.linkav20.habits.main.impl.mvi.main

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class MainBootstrapper : CoroutineBootstrapper<MainAction>() {

    override fun invoke() {
        dispatch(MainAction.Init)
    }
}
