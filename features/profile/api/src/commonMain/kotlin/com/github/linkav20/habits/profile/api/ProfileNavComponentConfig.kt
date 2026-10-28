package com.github.linkav20.habits.profile.api

import com.github.linkav20.habits.core.mvi.navigation.BaseNavigationConfig
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.github.linkav20.habits.profile.api.profile.ProfileComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileNavigationConfig : BaseNavigationConfig {
    @Serializable
    data object DebugMenu : ProfileNavigationConfig {
        override val screenName: String = "debug_menu_screen"
    }

    @Serializable
    data object Profile : ProfileNavigationConfig {
        override val screenName: String = "profile_screen"
    }
}

sealed class ProfileNavigationChild {
    data class DebugMenu(val component: DebugMenuComponent) : ProfileNavigationChild()

    data class Profile(
        val component: ProfileComponent,
    ) : ProfileNavigationChild()
}
