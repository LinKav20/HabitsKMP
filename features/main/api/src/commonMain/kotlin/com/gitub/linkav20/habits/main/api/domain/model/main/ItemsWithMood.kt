package com.gitub.linkav20.habits.main.api.domain.model.main

import com.github.linkav20.habits.core.uikit.components.charts.line.LineChartPoint
import com.github.linkav20.habits.core.utils.Time
import kotlinx.datetime.LocalDate

data class ItemsWithMood(
    val days: List<LocalDate>,
    val checkingItemWithCompletion: List<CheckingItemWithCompletion>,
    val mood: List<MoodItem>,
    val pagesCount: Int,
) {
    val daysToShow: Int = if (pagesCount > 0) days.size / pagesCount else 0

    fun getMoodEmoji(
        indexInPage: Int,
        page: Int
    ) = mood[page * daysToShow + indexInPage].rate.toEmoji()

    fun getDays(page: Int) = days.getSelectedWindow(page)

    fun getCompletionForItem(
        item: CheckingItemWithCompletion,
        page: Int,
    ) = item.completion.getSelectedWindow(page)

    // todo do it clear
    fun getChartPoints(page: Int) = listOf(
        LineChartPoint(
            x = 0f,
            y = ((mood[(page * daysToShow - 1).coerceAtLeast(0)].rate ?: 0)
                    + (mood[page * daysToShow].rate ?: 0).toFloat()) / 2,
        ),
    ).plus(
        mood.getSelectedWindow(page)
            .mapNotNull {
                if (it.date < Time.now()) {
                    LineChartPoint(
                        x = it.date.dayOfWeek.ordinal + 0.5f,
                        y = it.rate?.toFloat() ?: 0f,
                    )
                } else if (it.date == Time.now()) {
                    it.rate?.let { rate ->
                        LineChartPoint(
                            x = it.date.dayOfWeek.ordinal + 0.5f,
                            y = rate.toFloat(),
                        )
                    }
                } else {
                    null
                }
            },
    )

    private fun <T> List<T>.getSelectedWindow(page: Int): List<T> {
        val indices = page * daysToShow until (page + 1) * daysToShow
        return this.slice(indices)
    }
}
