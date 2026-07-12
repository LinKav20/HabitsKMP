package com.github.linkav20.habits.core.uikit.statusbar

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

public actual val statusBar: StatusBar = iOS

@Suppress("ClassNaming", "ClassName")
public object iOS : StatusBar {
    @Composable
    override fun SetStatusBarLightMode(isDarkStatusBarColor: Boolean) {
        val statusBarStyle = if (!isDarkStatusBarColor) {
            UIStatusBarStyleLightContent
        } else {
            UIStatusBarStyleDarkContent
        }
        UIApplication.sharedApplication.setStatusBarStyle(statusBarStyle)
    }
}
