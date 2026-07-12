package com.github.linkav20.habits.compose.notification

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import habits.composeapp.generated.resources.Res
import habits.composeapp.generated.resources.debug_notification_subtitle
import habits.composeapp.generated.resources.debug_notification_title
import habits.composeapp.generated.resources.prod_value
import habits.composeapp.generated.resources.stage_value
import org.jetbrains.compose.resources.getString
import splitties.init.directBootCtx

const val ACTION_NOTIFICATION = "NOTIFICATION_CLICK"
private const val CHANNEL_ID = "DEBUG_MENU_NOTIFICATION_CHANNEL"
private const val CHANNEL_NAME = "DEBUG_MENU_NOTIFICATION_CHANNEL_NAME"
private const val NOTIFICATION_ID = 1

private var receiver: DebugNotificationReceiver? = null

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
@SuppressLint("LaunchActivityFromNotification")
actual suspend fun showDebugMenuNotification(isStage: Boolean) {
    val appContext = directBootCtx.applicationContext

    val intent = Intent(ACTION_NOTIFICATION).apply {
        `package` = appContext.packageName
    }
    val pendingIntent = PendingIntent.getBroadcast(
        appContext,
        0,
        intent,
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE,
    )

    val envString = getString(
        if (isStage) {
            Res.string.stage_value
        } else {
            Res.string.prod_value
        },
    )

    val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_dialog_info)
        .setContentTitle(getString(Res.string.debug_notification_title))
        .setContentText(
            getString(
                Res.string.debug_notification_subtitle,
                envString,
            ),
        )
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager = NotificationManagerCompat.from(appContext)

    val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW,
    )
    notificationManager.createNotificationChannel(channel)
    builder.setChannelId(CHANNEL_ID)

    notificationManager.notify(NOTIFICATION_ID, builder.build())
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
actual suspend fun registerDebugMenuNotification(
    onClick: () -> Unit,
    showNotification: () -> Unit,
) {
    val appContext = directBootCtx.applicationContext
    if (ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        receiver ?: registerReceiver(onClick = { onClick() })
        showNotification()
    }
}

actual fun unregisterDebugMenuNotification() {
    unregisterReceiver(receiver)
    receiver = null
}
