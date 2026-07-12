package com.github.linkav20.habits

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.defaultComponentContext
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import com.github.linkav20.habits.compose.MainContent
import com.github.linkav20.habits.compose.presentation.root.RootComponent
import com.github.linkav20.habits.core.utils.ActivityStackFactory
import com.github.linkav20.habits.datastore.datasource.SettingsStorage

class MainActivity : ComponentActivity() {
    var navigationToDebugMenu: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        ActivityStackFactory.push(this)

        val rootFactory: RootComponent.Factory by inject()
        val root = rootFactory.create(defaultComponentContext())
        navigationToDebugMenu = { root.navigateToDebugMenu() }

        checkAndRequestMultiplePermissions(this)

        enableEdgeToEdge()
        setContent {
            MainContent(root)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        val activity = this

        navigationToDebugMenu?.let { openDebugMenu ->
            lifecycle.coroutineScope.launch {
                val settingsStorage: SettingsStorage by inject()
                val isStage = settingsStorage.getIsStage()

                showDebugMenuNotification(
                    activity = activity,
                    navigationToDebugMenu = openDebugMenu,
                    isStage = isStage,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onDestroy() {
        ActivityStackFactory.pop(this)
        super.onDestroy()
    }
}
