package com.github.linkav20.habits.core.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation

@Composable
public actual fun isLandscape(): Boolean {
    val orientation = UIDevice.currentDevice.orientation

    return when (orientation) {
        UIDeviceOrientation.UIDeviceOrientationLandscapeLeft,
        UIDeviceOrientation.UIDeviceOrientationLandscapeRight,
            -> true

        else -> false
    }
}
