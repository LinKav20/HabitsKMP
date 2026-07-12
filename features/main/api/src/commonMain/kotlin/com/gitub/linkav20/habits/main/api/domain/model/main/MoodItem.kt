package com.gitub.linkav20.habits.main.api.domain.model.main

import kotlinx.datetime.LocalDate

data class MoodItem(
    val rate: Int?,
    val date: LocalDate,
)

fun Int?.toEmoji(): String =
    when (this) {
        0 -> "\uD83D\uDE2D"
        1 -> "\uD83D\uDE20"
        2 -> "\uD83D\uDE16"
        3 -> "\uD83D\uDE14"
        4 -> "\uD83D\uDE41"
        5 -> "\uD83D\uDE42"
        6 -> "\uD83D\uDE0C"
        7 -> "\uD83D\uDE01"
        8 -> "\uD83E\uDD70"
        9 -> "\uD83E\uDD29"
        else -> ""
    }
