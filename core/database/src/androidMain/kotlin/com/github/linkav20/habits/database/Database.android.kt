package com.github.linkav20.habits.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import com.github.linkav20.habits.core.utils.currentEnvironment
import com.github.linkav20.habits.database.habits.HabitsDatabase
import com.github.linkav20.habits.database.mood.MoodDatabase
import splitties.init.directBootCtx

actual fun getHabitDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase> {
    val appContext = directBootCtx.applicationContext
    val dbFile = appContext.getDatabasePath(HABITS_DATABASE_NAME)
    val dbBuilder = Room.databaseBuilder<HabitsDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
    if (currentEnvironment.isDebug) {
        // В debug-сборке разрешено удаление таблиц при отсутствии миграций
        dbBuilder.fallbackToDestructiveMigration(dropAllTables = false)
    }
    return dbBuilder
}

actual fun getMoodDatabaseBuilder(): RoomDatabase.Builder<MoodDatabase> {
    val appContext = directBootCtx.applicationContext
    val dbFile = appContext.getDatabasePath(MOOD_DATABASE_NAME)
    val dbBuilder = Room.databaseBuilder<MoodDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
    if (currentEnvironment.isDebug) {
        // В debug-сборке разрешено удаление таблиц при отсутствии миграций
        dbBuilder.fallbackToDestructiveMigration(dropAllTables = false)
    }
    return dbBuilder
}
