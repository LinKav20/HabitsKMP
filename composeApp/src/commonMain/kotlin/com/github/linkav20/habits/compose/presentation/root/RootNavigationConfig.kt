package com.github.linkav20.habits.compose.presentation.root

import kotlinx.serialization.Serializable
import com.github.linkav20.habits.auth.api.AuthComponent
import com.github.linkav20.habits.core.mvi.navigation.BaseNavigationConfig
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.gitub.linkav20.habits.main.api.MainNavComponent

@Serializable
sealed interface RootNavigationConfig : BaseNavigationConfig {
    @Serializable
    data object Auth : RootNavigationConfig {
        override val screenName: String = "auth"
    }

    @Serializable
    data object Main : RootNavigationConfig {
        override val screenName: String = "main"
    }

    @Serializable
    data object DebugMenu : RootNavigationConfig {
        override val screenName: String = "debug_menu"
    }
}

sealed class RootNavigationChild {
    class Auth(
        val component: AuthComponent,
    ) : RootNavigationChild()

    class Main(
        val component: MainNavComponent,
    ) : RootNavigationChild()

    class DebugMenu(
        val component: DebugMenuComponent,
    ) : RootNavigationChild()
}
