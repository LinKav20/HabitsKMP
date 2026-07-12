package com.github.linkav20.habits.core.utils

import platform.UIKit.UIApplication
import platform.UIKit.setStatusBarHidden

public actual fun setStatusBarVisibility(isVisible: Boolean) {
    UIApplication.sharedApplication.setStatusBarHidden(!isVisible)
}
