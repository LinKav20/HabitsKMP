package com.github.linkav20.habits.profile.api.profile

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface ProfileComponent {
    fun onBackClicked()

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onBackClick: () -> Unit,
        ): ProfileComponent
    }
}
