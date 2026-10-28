package com.gitub.linkav20.habits.main.impl.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.github.linkav20.habits.createitem.api.AddHabitComponent
import com.github.linkav20.habits.habit.api.HabitComponent
import com.github.linkav20.habits.profile.api.ProfileNavComponent
import com.github.linkav20.habits.todo.api.TodoComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import com.gitub.linkav20.habits.main.api.main.MainComponent
import com.gitub.linkav20.habits.main.api.MainNavComponent
import com.gitub.linkav20.habits.main.api.MainNavigationChild
import com.gitub.linkav20.habits.main.api.MainNavigationConfig
import com.gitub.linkav20.habits.main.api.communication.GlobalCommunicationEvent
import com.gitub.linkav20.habits.main.impl.communication.GlobalCommunication
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultMainNavComponent private constructor(
    componentContext: ComponentContext,
    private val mainComponentFactory: MainComponent.Factory,
    private val addHabitComponentFactory: AddHabitComponent.Factory,
    private val habitComponentFactory: HabitComponent.Factory,
    private val todoComponentFactory: TodoComponent.Factory,
    private val profileNavComponentFactory: ProfileNavComponent.Factory,
    private val globalCourseCommunication: GlobalCommunication,
) : MainNavComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<MainNavigationConfig>()
    private val scope = coroutineScope()

    override val stack: Value<ChildStack<*, MainNavigationChild>> =
        componentContext.childStack(
            source = navigation,
            serializer = MainNavigationConfig.serializer(),
            initialConfiguration = MainNavigationConfig.Main,
            handleBackButton = true,
            childFactory = ::child,
        )


    override fun onBackClicked() {
        navigation.pop()
    }

    private fun child(
        config: MainNavigationConfig,
        childComponentContext: ComponentContext,
    ): MainNavigationChild {
        return when (config) {
            is MainNavigationConfig.Main -> {
                MainNavigationChild.Main(
                    component = mainComponentFactory.create(
                        componentContext = childComponentContext,
                        onProfileClick = {
                            scope.launch {
                                navigation.pushNew(MainNavigationConfig.Profile)
                            }
                        },
                        onAddItemClick = {
                            scope.launch {
                                navigation.pushNew(MainNavigationConfig.AddItem)
                            }
                        },
                        onViewHabitClick = { id ->
                            scope.launch {
                                navigation.pushNew(MainNavigationConfig.ViewHabit(id))
                            }
                        },
                        onViewTodoClick = { id ->
                            scope.launch {
                                navigation.pushNew(MainNavigationConfig.ViewTodo(id))
                            }
                        },
                    ),
                )
            }

            is MainNavigationConfig.Profile -> MainNavigationChild.Profile(
                component = profileNavComponentFactory.create(
                    componentContext = childComponentContext,
                    onBackClick = ::onBackClicked,
                ),
            )

            is MainNavigationConfig.AddItem -> {
                MainNavigationChild.AddItem(
                    component = addHabitComponentFactory.create(
                        componentContext = childComponentContext,
                        onBackClick = {
                            onBackClicked()
                            scope.launch {
                                globalCourseCommunication.sendEvent(GlobalCommunicationEvent.DataUpdated)
                            }
                        },
                    ),
                )
            }

            is MainNavigationConfig.ViewTodo -> MainNavigationChild.ViewTodo(
                component = todoComponentFactory.create(
                    componentContext = childComponentContext,
                    onBackClick = ::onBackClicked,
                    itemId = config.todoId,
                ),
            )

            is MainNavigationConfig.ViewHabit -> MainNavigationChild.ViewHabit(
                component = habitComponentFactory.create(
                    componentContext = childComponentContext,
                    onBackClick = ::onBackClicked,
                    itemId = config.habitId,
                ),
            )
        }
    }

    internal class Factory :
        MainNavComponent.Factory,
        KoinComponent {

        override fun create(
            componentContext: ComponentContext,
        ): MainNavComponent {
            return DefaultMainNavComponent(
                componentContext = componentContext,
                mainComponentFactory = get(),
                addHabitComponentFactory = get(),
                habitComponentFactory = get(),
                todoComponentFactory = get(),
                profileNavComponentFactory = get(),
                globalCourseCommunication = get(),
            )
        }
    }
}
