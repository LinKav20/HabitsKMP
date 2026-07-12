package com.github.linkav20.habits.core.utils

import platform.Foundation.NSBundle

public fun getAppVersion(): String {
    return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "Unknown"
}
