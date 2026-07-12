package com.github.linkav20.habits.auth.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.linkav20.habits.auth.api.AuthComponent
import com.github.linkav20.habits.auth.api.AuthContent

internal class AuthContentImpl : AuthContent {
    @Composable
    override fun Content(
        component: AuthComponent,
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("AuthContent")
            Button(onClick = { component.onAuthorizedClicked() }) {
                Text("Go to content")
            }
        }
    }
}
