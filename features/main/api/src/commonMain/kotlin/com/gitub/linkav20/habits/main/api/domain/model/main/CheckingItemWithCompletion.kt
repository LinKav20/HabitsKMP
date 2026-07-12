package com.gitub.linkav20.habits.main.api.domain.model.main

import kotlinx.datetime.LocalDate

data class CheckingItemWithCompletion(
    val checkingItem: CheckingItem,
    val completion: List<Pair<LocalDate, Boolean?>>,
)
