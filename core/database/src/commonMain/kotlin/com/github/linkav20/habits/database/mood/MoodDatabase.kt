package com.github.linkav20.habits.database.mood

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.github.linkav20.habits.database.converters.DateConverters
import com.github.linkav20.habits.database.mood.dao.MoodDao
import com.github.linkav20.habits.database.mood.model.MoodEntity

@Database(
    entities = [MoodEntity::class],
    version = 1,
)
@ConstructedBy(MoodDatabaseConstructor::class)
@TypeConverters(
    DateConverters::class,
)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun getDao(): MoodDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MoodDatabaseConstructor : RoomDatabaseConstructor<MoodDatabase> {
    override fun initialize(): MoodDatabase
}
