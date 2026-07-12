package com.github.linkav20.habits.database.mood.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.linkav20.habits.database.mood.model.MoodEntity
import com.github.linkav20.habits.database.mood.model.Tables

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity)

    @Query(
        """
        SELECT * FROM ${Tables.MOOD}
        WHERE date BETWEEN :startDate AND :endDate
    """,
    )
    suspend fun getMoods(
        startDate: String,
        endDate: String,
    ): List<MoodEntity>
}
