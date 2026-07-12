package com.github.linkav20.habits.core.utils

internal const val CLIENT_TYPE_ANDROID: String = "android"
internal const val CLIENT_TYPE_IOS: String = "ios"

public interface Environment {
    public val isDebug: Boolean
    public val clientType: String
    public val osName: String
    public val osVersion: String
    public val deviceId: String
    public val deviceName: String
    public val appVersion: String
    public val isExplicitLanguageConfigSupported: Boolean
    public val isExplicitHeightForWebViewRequired: Boolean

    public suspend fun getIpAddress(): String?
    public fun getCurrentTimeMillis(): Long
}

public fun Environment.isAndroid(): Boolean = clientType == CLIENT_TYPE_ANDROID

public expect val currentEnvironment: Environment
