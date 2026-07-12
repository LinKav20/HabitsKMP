package com.gitub.linkav20.habits.main.api.domain.model.main

import kotlinx.datetime.LocalDate

interface CheckingItem {
    val id: String
    val title: String
    val endDate: LocalDate?
}
