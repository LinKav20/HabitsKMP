package com.github.linkav20.habits.auth.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

@Stable
interface AuthComponent {
//    val stack: Value<ChildStack<*, AuthNavigationChild>>
    val model: StateFlow<AuthStore.State>

    fun onAuthorizedClicked()

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onAuthorizedClicked: () -> Unit,
            onBackPressed: () -> Unit,
        ): AuthComponent
    }
}
