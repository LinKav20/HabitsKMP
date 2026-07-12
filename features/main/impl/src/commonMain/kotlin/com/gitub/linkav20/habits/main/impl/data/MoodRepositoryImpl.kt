package com.gitub.linkav20.habits.main.impl.data

import com.github.linkav20.habits.database.mood.dao.MoodDao
import com.github.linkav20.habits.database.mood.model.MoodEntity
import com.gitub.linkav20.habits.main.api.domain.model.main.MoodItem
import com.gitub.linkav20.habits.main.impl.data.mapper.toDomain
import com.gitub.linkav20.habits.main.impl.domain.repository.MoodRepository
import kotlinx.datetime.LocalDate

class MoodRepositoryImpl(
    private val moodDao: MoodDao,
) : MoodRepository {
    override suspend fun getMood(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Result<List<MoodItem>> = runCatching {
        moodDao.getMoods(
            startDate = startDate.toString(),
            endDate = endDate.toString(),
        ).map { it.toDomain() }
    }

    override suspend fun setMood(rate: Int, date: LocalDate) = runCatching {
        moodDao.insertMood(
            MoodEntity(
                rate = rate,
                date = date,
            ),
        )
    }
}
