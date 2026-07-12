package com.github.linkav20.habits.network.mapper

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException
import com.github.linkav20.habits.common.model.NetworkException

@Suppress("ThrowsCount")
fun validateHttpResponse(response: HttpResponse) {
    when (val statusCode = response.status.value) {
        in 200..299 -> return
        502 -> throw NetworkException.Connection
        504 -> throw NetworkException.Timeout
        in 400..499 -> throw NetworkException.Client(statusCode)
        in 500..599 -> throw NetworkException.Server(statusCode)
        else -> throw NetworkException.Unknown
    }
}

fun mapNetworkThrowableToDomain(throwable: Throwable): Throwable =
    when (throwable) {
        is ConnectTimeoutException -> NetworkException.Timeout
        is SocketTimeoutException -> NetworkException.Timeout
        is CancellationException -> throwable
        else -> throwable.mapPlatformSpecificNetworkError()
    }

expect fun Throwable.mapPlatformSpecificNetworkError(): NetworkException
