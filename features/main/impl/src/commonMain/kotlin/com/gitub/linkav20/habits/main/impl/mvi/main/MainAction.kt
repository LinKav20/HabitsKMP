package com.gitub.linkav20.habits.main.impl.mvi.main

internal sealed interface MainAction {
    data object Init : MainAction
}
