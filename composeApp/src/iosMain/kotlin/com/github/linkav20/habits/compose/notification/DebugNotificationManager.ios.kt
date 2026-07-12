package com.github.linkav20.habits.compose.notification

import habits.composeapp.generated.resources.Res
import habits.composeapp.generated.resources.debug_notification_subtitle
import habits.composeapp.generated.resources.debug_notification_title
import habits.composeapp.generated.resources.prod_value
import habits.composeapp.generated.resources.stage_value
import org.jetbrains.compose.resources.getString
import platform.Foundation.NSDate
import platform.Foundation.date
import platform.Foundation.timeIntervalSince1970
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

private var notificationDelegate: NotificationDelegate? = null

actual suspend fun showDebugMenuNotification(isStage: Boolean) {
    scheduleNotification(isStage)
}

actual suspend fun registerDebugMenuNotification(
    onClick: () -> Unit,
    showNotification: () -> Unit,
) {
    val center = UNUserNotificationCenter.currentNotificationCenter()

    if (notificationDelegate == null) {
        notificationDelegate = NotificationDelegate()
        center.delegate = notificationDelegate
    }

    notificationDelegate?.onNotificationClick = onClick

    center.requestAuthorizationWithOptions(
        UNAuthorizationOptionAlert or UNAuthorizationOptionSound,
        completionHandler = { granted, _ ->
            if (granted) {
                showNotification()
            }
        },
    )
}

private suspend fun scheduleNotification(isStage: Boolean) {
    val center = UNUserNotificationCenter.currentNotificationCenter()
    val envString = getString(
        if (isStage) {
            Res.string.stage_value
        } else {
            Res.string.prod_value
        },
    )

    val content = UNMutableNotificationContent().apply {
        setTitle(getString(Res.string.debug_notification_title))
        setBody(
            getString(
                Res.string.debug_notification_subtitle,
                envString,
            ),
        )
    }

    val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, false)

    val request = UNNotificationRequest.requestWithIdentifier(
        "debug_notification_${NSDate.date().timeIntervalSince1970}",
        content,
        trigger,
    )

    center.addNotificationRequest(request) {}
}

actual fun unregisterDebugMenuNotification() {
    UNUserNotificationCenter.currentNotificationCenter().delegate = null
    notificationDelegate = null
}
