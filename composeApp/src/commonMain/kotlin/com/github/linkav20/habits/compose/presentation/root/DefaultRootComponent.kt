package com.github.linkav20.habits.compose.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.github.linkav20.habits.compose.notification.registerDebugMenuNotification
import com.github.linkav20.habits.compose.notification.showDebugMenuNotification
import com.github.linkav20.habits.compose.notification.unregisterDebugMenuNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import com.github.linkav20.habits.auth.api.AuthComponent
import com.github.linkav20.habits.core.uikit.components.snackbar.SnackBarNotifier
import com.github.linkav20.habits.core.uikit.components.snackbar.HabitSnackBarData
import com.github.linkav20.habits.core.utils.currentEnvironment
import com.github.linkav20.habits.datastore.datasource.SettingsStorage
import com.github.linkav20.habits.datastore.datasource.ThemePreference
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.github.linkav20.habits.network.GlobalAuthChannel
import com.github.linkav20.habits.network.di.authorizedScope
import com.gitub.linkav20.habits.main.api.MainNavComponent

class DefaultRootComponent private constructor(
    componentContext: ComponentContext,
    private val authComponentFactory: AuthComponent.Factory,
    private val mainComponentFactory: MainNavComponent.Factory,
    private val debugMenuFactory: DebugMenuComponent.Factory,
    private val authChannel: GlobalAuthChannel,
    private val settingsStorage: SettingsStorage,
    private val snackBarNotifier: SnackBarNotifier,
) : RootComponent,
    ComponentContext by componentContext,
    BackHandlerOwner,
    KoinComponent {

    private val scope = coroutineScope()
    private val navigation = StackNavigation<RootNavigationConfig>()

    val startConfiguration: RootNavigationConfig = runBlocking {
        if (settingsStorage.getToken().isNullOrEmpty()) {
            RootNavigationConfig.Main // todo replace to auth
        } else {
            RootNavigationConfig.Main
        }
    }

    private val themePreferences = instanceKeeper.getOrCreate {
        ThemePreferences(initialTheme = getThemeFromStorageFirstTime())
    }

    override val stack: Value<ChildStack<*, RootNavigationChild>> = childStack(
        source = navigation,
        serializer = RootNavigationConfig.serializer(),
        initialConfiguration = startConfiguration,
        handleBackButton = true,
        childFactory = ::child,
    )

    init {
        if (currentEnvironment.isDebug) {
            scope.launch {
                registerDebugMenuNotification(
                    onClick = { navigateToDebugMenu() },
                    showNotification = {
                        scope.launch {
                            val isStage = settingsStorage.getIsStage()
                            showDebugMenuNotification(isStage)
                        }
                    },
                )
            }
            lifecycle.doOnDestroy {
                unregisterDebugMenuNotification()
            }
        }

        scope.launch {
            authorizedScope().close()
            authorizedScope()
            authChannel.unauthorizedEvents.collect {
                navigation.replaceAll(RootNavigationConfig.Auth)
            }
        }
        scope.launch {
            settingsStorage.getTheme().collect {
                themePreferences.theme.value = it
            }
        }
    }

    override val theme: StateFlow<ThemePreference> = themePreferences.theme.asStateFlow()
    override val snackBarFlow: Flow<HabitSnackBarData> = snackBarNotifier.messages

    override fun onBackClicked() {
        navigation.pop()
    }

    override fun navigateToDebugMenu() {
        navigation.pushNew(RootNavigationConfig.DebugMenu)
    }

    private fun child(
        config: RootNavigationConfig,
        childComponentContext: ComponentContext,
    ): RootNavigationChild {
        return when (config) {
            is RootNavigationConfig.Auth ->
                createAuthComponentFactory(childComponentContext)

            is RootNavigationConfig.Main ->
                createMainComponent(childComponentContext)

            is RootNavigationConfig.DebugMenu ->
                createDebugMenuComponentFactory(childComponentContext)
        }
    }

    private fun createAuthComponentFactory(childComponentContext: ComponentContext) =
        RootNavigationChild.Auth(
            authComponentFactory.create(
                componentContext = childComponentContext,
                onAuthorizedClicked = {
                    scope.launch {
                        navigation.replaceAll(RootNavigationConfig.Main)
                    }
                },
                onBackPressed = {},
            ),
        )

    private fun createDebugMenuComponentFactory(childComponentContext: ComponentContext) =
        RootNavigationChild.DebugMenu(
            debugMenuFactory.create(
                componentContext = childComponentContext,
                onShowNotification = {
                    scope.launch {
                        val isStage = settingsStorage.getIsStage()
                        showDebugMenuNotification(isStage)
                    }
                },
                onBackClick = { navigation.pop() },
            ),
        )

    private fun createMainComponent(childComponentContext: ComponentContext): RootNavigationChild.Main {
        return RootNavigationChild.Main(
            mainComponentFactory.create(childComponentContext),
        )
    }

    private fun getThemeFromStorageFirstTime() =
        runBlocking {
            settingsStorage.getTheme().first()
        }

    private class ThemePreferences(
        initialTheme: ThemePreference,
    ) : InstanceKeeper.Instance {
        val theme = MutableStateFlow(initialTheme)
    }

    class Factory :
        RootComponent.Factory,
        KoinComponent {

        init {
            authorizedScope()
        }

        override fun create(componentContext: ComponentContext): RootComponent {
            return DefaultRootComponent(
                componentContext,
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
    }
}
