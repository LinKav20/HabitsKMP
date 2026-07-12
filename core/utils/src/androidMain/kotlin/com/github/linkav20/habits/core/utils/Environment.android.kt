package com.github.linkav20.habits.core.utils

import android.os.Build

public actual val currentEnvironment: Environment = AndroidEnvironment

public object AndroidEnvironment : Environment {
    override val isDebug: Boolean
        get() = true // todo

    override val clientType: String = CLIENT_TYPE_ANDROID

    override val osName: String = "Android"

    override val osVersion: String
        get() = Build.VERSION.RELEASE

    override val deviceId: String
        get() = DeviceInfoFactory.getDeviceId() ?: randomUUID()

    override val deviceName: String
        get() = DeviceInfoFactory.getDeviceName()

    override val appVersion: String
        get() = getAppVersion()

    override val isExplicitLanguageConfigSupported: Boolean = true

    override val isExplicitHeightForWebViewRequired: Boolean = false

    override suspend fun getIpAddress(): String? {
        return DeviceInfoFactory.getIpAddress()
    }

    override fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
