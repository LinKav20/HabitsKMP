package com.github.linkav20.habits.network.mapper

import com.github.linkav20.habits.common.model.NetworkException
import java.net.UnknownHostException

actual fun Throwable.mapPlatformSpecificNetworkError(): NetworkException =
    when (this) {
    is UnknownHostException -> NetworkException.UnknownHostException
    else -> NetworkException.Unknown
}
