package com.github.linkav20.habits.compose.notification

expect suspend fun showDebugMenuNotification(isStage: Boolean)

expect suspend fun registerDebugMenuNotification(
    onClick: () -> Unit,
    showNotification: () -> Unit,
)

expect fun unregisterDebugMenuNotification()
