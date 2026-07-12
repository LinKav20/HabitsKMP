package com.github.linkav20.habits.auth.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.serialization.Serializable
import com.github.linkav20.habits.core.mvi.navigation.BaseNavigationConfig

@Serializable
sealed interface AuthNavigationConfig : BaseNavigationConfig {
    @Serializable
    data object PhoneAuth : AuthNavigationConfig {
        override val screenName: String = "phone_auth"
    }

    @Serializable
    data object SmsVerify : AuthNavigationConfig {
        override val screenName: String = "sms_verify"
    }
}

sealed class AuthNavigationChild {
    class PhoneAuth(
        val childComponentContext: ComponentContext,
        val component: AuthComponent,
    ) : AuthNavigationChild()

    class SmsVerify(
        val childComponentContext: ComponentContext,
        val component: AuthComponent,
    ) : AuthNavigationChild()
}
