package com.github.linkav20.habits.network.mapper

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import com.github.linkav20.habits.common.model.NetworkException

actual fun Throwable.mapPlatformSpecificNetworkError(): NetworkException =
    when (this) {
    is DarwinHttpRequestException -> {
        if (message?.contains("SSL") == true) {
            NetworkException.UnknownHostException
        } else {
            NetworkException.Unknown
        }
    }
    else -> NetworkException.Unknown
}
