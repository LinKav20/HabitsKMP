package com.github.linkav20.habits.database.habits.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.linkav20.habits.database.habits.model.habit.HabitEntity
import com.github.linkav20.habits.database.habits.model.ItemCompletion
import com.github.linkav20.habits.database.habits.model.Tables

@Dao
interface HabitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("""
        SELECT * FROM ${Tables.HABITS}
        WHERE endDate IS NULL OR endDate >= :date
    """,
    )
    suspend fun getActiveHabitsOnDate(date: String): List<HabitEntity>

    @Query("DELETE FROM ${Tables.HABITS}")
    suspend fun clearAll()
}
