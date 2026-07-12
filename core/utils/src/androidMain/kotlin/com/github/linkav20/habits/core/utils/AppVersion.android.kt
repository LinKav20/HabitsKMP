package com.github.linkav20.habits.core.utils

import splitties.init.directBootCtx

public fun getAppVersion(): String {
    val packageInfo = directBootCtx.packageManager.getPackageInfo(directBootCtx.packageName, 0)
    return packageInfo.versionName ?: "Unknown"
}
