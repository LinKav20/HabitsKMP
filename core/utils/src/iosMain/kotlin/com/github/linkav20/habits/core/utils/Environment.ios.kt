package com.github.linkav20.habits.core.utils

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

public actual val currentEnvironment: Environment = iOSEnvironment

@OptIn(ExperimentalNativeApi::class)
@Suppress("ClassNaming", "ClassName")
public object iOSEnvironment : Environment {
    override val isDebug: Boolean
        get() = Platform.isDebugBinary

    override val clientType: String = CLIENT_TYPE_IOS

    override val osName: String
        get() = UIDevice.currentDevice.systemName

    override val osVersion: String
        get() = UIDevice.currentDevice.systemVersion

    override val deviceId: String
        get() = DeviceInfoFactory.getDeviceId() ?: randomUUID()

    override val deviceName: String
        get() = DeviceInfoFactory.getDeviceName()

    override val appVersion: String
        get() = getAppVersion()

    override val isExplicitLanguageConfigSupported: Boolean = false

    override val isExplicitHeightForWebViewRequired: Boolean = true

    override suspend fun getIpAddress(): String? {
        return DeviceInfoFactory.getIpAddress()
    }

    override fun getCurrentTimeMillis(): Long {
        return (NSDate().timeIntervalSince1970() * 1000).toLong()
    }
}
