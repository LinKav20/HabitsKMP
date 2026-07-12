package com.github.linkav20.habits.compose.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import splitties.init.directBootCtx

class DebugNotificationReceiver(private val onClick: () -> Unit) : BroadcastReceiver() {

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        if (intent?.action == ACTION_NOTIFICATION) {
            onClick()
        }
    }
}

fun registerReceiver(onClick: () -> Unit): DebugNotificationReceiver {
    val receiver = DebugNotificationReceiver(onClick = { onClick() })
    val filter = IntentFilter(ACTION_NOTIFICATION)

    ContextCompat.registerReceiver(
        directBootCtx.applicationContext,
        receiver,
        filter,
        ContextCompat.RECEIVER_NOT_EXPORTED,
    )
    return receiver
}

fun unregisterReceiver(receiver: BroadcastReceiver?) {
    val appContext = directBootCtx.applicationContext
    receiver?.let {
        appContext.unregisterReceiver(receiver)
    }
}
