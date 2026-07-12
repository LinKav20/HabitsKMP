package com.github.linkav20.habits

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import com.github.linkav20.habits.compose.notification.registerDebugMenuNotification
import com.github.linkav20.habits.compose.notification.showDebugMenuNotification
import com.github.linkav20.habits.core.utils.currentEnvironment

private const val MULTIPLE_PERMISSIONS_REQUEST_CODE = 1002

private val REQUIRED_PERMISSIONS = buildList {
    if (currentEnvironment.isDebug && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

fun checkAndRequestMultiplePermissions(activity: Activity) {
    val permissionsToRequest = REQUIRED_PERMISSIONS.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            activity,
            permissionsToRequest.toTypedArray(),
            MULTIPLE_PERMISSIONS_REQUEST_CODE,
        )
    }
}

suspend fun showDebugMenuNotification(
    activity: Activity,
    isStage: Boolean,
    navigationToDebugMenu: (() -> Unit),
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        registerDebugMenuNotification(
            onClick = { navigationToDebugMenu() },
            showNotification = {
                MainScope().launch {
                    showDebugMenuNotification(isStage)
                }
            },
        )
    }
}
