package com.gitub.linkav20.habits.main.api

import com.github.linkav20.habits.core.mvi.navigation.BaseNavigationConfig
import com.github.linkav20.habits.createitem.api.AddHabitComponent
import com.github.linkav20.habits.habit.api.HabitComponent
import com.github.linkav20.habits.profile.api.ProfileNavComponent
import com.github.linkav20.habits.todo.api.TodoComponent
import com.gitub.linkav20.habits.main.api.main.MainComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavigationConfig : BaseNavigationConfig {
    @Serializable
    data object Main : MainNavigationConfig {
        override val screenName: String = "main_screen"
    }

    @Serializable
    data object Profile : MainNavigationConfig {
        override val screenName: String = "profile"
    }

    data class ViewHabit(
        val habitId: String,
    ) : MainNavigationConfig {
        override val screenName: String = "view_habit"
    }

    data class ViewTodo(
        val todoId: String,
    ) : MainNavigationConfig {
        override val screenName: String = "view_todo"
    }

    data object AddItem : MainNavigationConfig {
        override val screenName: String = "add_item"
    }
}

sealed class MainNavigationChild {
    class Main(
        val component: MainComponent,
    ) : MainNavigationChild()

    data class Profile(
        val component: ProfileNavComponent,
    ) : MainNavigationChild()

    data class AddItem(
        val component: AddHabitComponent,
    ) : MainNavigationChild()

    data class ViewHabit(
        val component: HabitComponent,
    ) : MainNavigationChild()

    data class ViewTodo(
        val component: TodoComponent,
    ) : MainNavigationChild()
}
