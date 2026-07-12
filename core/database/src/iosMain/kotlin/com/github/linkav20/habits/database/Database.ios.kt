package com.github.linkav20.habits.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import com.github.linkav20.habits.core.utils.currentEnvironment
import com.github.linkav20.habits.database.habits.HabitsDatabase
import com.github.linkav20.habits.database.mood.MoodDatabase

actual fun getHabitDatabaseBuilder(): RoomDatabase.Builder<HabitsDatabase> {
    val dbFilePath = documentDirectory() + "/${HABITS_DATABASE_NAME}"
    val dbBuilder = Room.databaseBuilder<HabitsDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
    if (currentEnvironment.isDebug) {
        // В debug-сборке разрешено удаление таблиц при отсутствии миграций
        dbBuilder.fallbackToDestructiveMigration(dropAllTables = false)
    }
    return dbBuilder
}

actual fun getMoodDatabaseBuilder(): RoomDatabase.Builder<MoodDatabase> {
    val dbFilePath = documentDirectory() + "/${MOOD_DATABASE_NAME}"
    val dbBuilder = Room.databaseBuilder<MoodDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
    if (currentEnvironment.isDebug) {
        // В debug-сборке разрешено удаление таблиц при отсутствии миграций
        dbBuilder.fallbackToDestructiveMigration(dropAllTables = false)
    }
    return dbBuilder
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
