package com.github.linkav20.habits.compose.di

import com.github.linkav20.habit.createitem.impl.di.createItemFeatureModule
import com.github.linkav20.habits.auth.impl.di.authFeatureModule
import com.github.linkav20.habits.core.utils.di.utilsModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import com.github.linkav20.habits.database.di.databaseModule
import com.github.linkav20.habits.datastore.di.dataStoreModule
import com.github.linkav20.habits.debugmenu.impl.di.debugMenuFeatureModule
import com.github.linkav20.habits.habit.impl.di.createHabitFeatureModule
import com.github.linkav20.habits.network.di.networkModule
import com.github.linkav20.habits.todo.impl.di.createTodoFeatureModule
import com.gitub.linkav20.habits.main.impl.di.mainFeatureModule

fun initKoin(externalModules: List<Module> = emptyList()) {
    startKoin {
        modules(
            authFeatureModule(),
            debugMenuFeatureModule(),
            mainFeatureModule(),
            createItemFeatureModule(),
            utilsModule(),
            createHabitFeatureModule(),
            createTodoFeatureModule(),
            rootComponentsModule,
            dataStoreModule,
            networkModule,
            databaseModule,
        )
        modules(externalModules)
    }
}
