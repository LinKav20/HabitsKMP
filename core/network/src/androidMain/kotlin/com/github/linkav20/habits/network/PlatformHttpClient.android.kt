package com.github.linkav20.habits.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

@Suppress(names = ["FunctionName"])
actual fun PlatformHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(block)
}
