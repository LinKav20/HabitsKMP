package com.github.linkav20.habits.todo.impl.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.linkav20.habits.todo.api.TodoComponent
import com.github.linkav20.habits.todo.api.TodoContent
import com.github.linkav20.habits.todo.api.TodoStore
import com.github.linkav20.habits.todo.impl.mvi.TodoStoreFactoryImpl
import com.github.linkav20.habits.todo.impl.ui.DefaultTodoComponent
import com.github.linkav20.habits.todo.impl.ui.TodoContentImpl
import org.koin.dsl.module

fun createTodoFeatureModule() =
    module {
        factory<TodoComponent.Factory> {
            DefaultTodoComponent.Factory(get())
        }

        factory<TodoContent> {
            TodoContentImpl()
        }

        factory<TodoStore.Factory> {
            TodoStoreFactoryImpl(
                DefaultStoreFactory(),
            )
        }
    }
