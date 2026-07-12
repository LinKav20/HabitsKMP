package com.github.linkav20.habits.database

import androidx.room.RoomDatabase
import com.github.linkav20.habits.database.habits.HabitsDatabase
import com.github.linkav20.habits.database.mood.MoodDatabase

internal const val HABITS_DATABASE_NAME = "habits.db"
internal const val MOOD_DATABASE_NAME = "mood.db"

fun getHabitDatabase(): HabitsDatabase = getHabitDatabaseBuilder().build()
expect fun getHabitDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase>

fun getMoodDatabase(): MoodDatabase = getMoodDatabaseBuilder().build()
expect fun getMoodDatabaseBuilder(): RoomDatabase.Builder<MoodDatabase>
