package com.github.linkav20.habits.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

@Suppress("FunctionName")
expect fun PlatformHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient
