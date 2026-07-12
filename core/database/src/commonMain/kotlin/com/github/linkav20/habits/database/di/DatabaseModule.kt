package com.github.linkav20.habits.database.di

import com.github.linkav20.habits.database.habits.HabitsDatabase
import com.github.linkav20.habits.database.getHabitDatabase
import com.github.linkav20.habits.database.getMoodDatabase
import com.github.linkav20.habits.database.mood.MoodDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { getHabitDatabase() }
    single { get<HabitsDatabase>().getHabitDao() }
    single { get<HabitsDatabase>().getTodoDao() }
    single { get<HabitsDatabase>().getCompletionDao() }

    single { getMoodDatabase() }
    single { get<MoodDatabase>().getDao() }
}
