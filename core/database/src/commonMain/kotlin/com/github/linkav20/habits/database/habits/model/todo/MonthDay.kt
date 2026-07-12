package com.github.linkav20.habits.database.habits.model.todo

import kotlinx.serialization.Serializable

@Serializable
data class MonthDay(
    val day: Int,
    val month: Int,
)
