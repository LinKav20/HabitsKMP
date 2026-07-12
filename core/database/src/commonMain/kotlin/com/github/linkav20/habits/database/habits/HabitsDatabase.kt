package com.github.linkav20.habits.database.habits

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.github.linkav20.habits.database.converters.DateConverters
import com.github.linkav20.habits.database.converters.DayOfWeekConverters
import com.github.linkav20.habits.database.converters.TodoRepeatRuleConverter
import com.github.linkav20.habits.database.habits.dao.CompletionDao
import com.github.linkav20.habits.database.habits.dao.HabitsDao
import com.github.linkav20.habits.database.habits.dao.TodoDao
import com.github.linkav20.habits.database.habits.model.habit.HabitEntity
import com.github.linkav20.habits.database.habits.model.ItemCompletion
import com.github.linkav20.habits.database.habits.model.todo.TodoEntity

@Database(
    entities = [
        HabitEntity::class,
        ItemCompletion::class,
        TodoEntity::class,
    ],
    version = 1,
)
@ConstructedBy(HabitsDatabaseConstructor::class)
@TypeConverters(
    DateConverters::class,
    DayOfWeekConverters::class,
    TodoRepeatRuleConverter::class,
)
abstract class HabitsDatabase : RoomDatabase() {
    abstract fun getHabitDao(): HabitsDao

    abstract fun getTodoDao(): TodoDao

    abstract fun getCompletionDao(): CompletionDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object HabitsDatabaseConstructor : RoomDatabaseConstructor<HabitsDatabase> {
    override fun initialize(): HabitsDatabase
}
