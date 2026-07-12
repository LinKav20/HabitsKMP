package com.github.linkav20.habits.network

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.github.linkav20.habits.common.usecase.UnauthUseCase
import com.github.linkav20.habits.datastore.datasource.SettingsStorage
import com.github.linkav20.habits.network.api.auth.createAuthApi
import com.github.linkav20.habits.network.mapper.mapNetworkThrowableToDomain
import com.github.linkav20.habits.network.mapper.validateHttpResponse

class TemplateNetworkClient internal constructor(
    private val baseUrl: String,
    private val unauthorizedHttpClient: HttpClient,
    val authorizedHttpClient: HttpClient,
) {
    private val ktorfitNoAuth by lazy {
        Ktorfit
            .Builder()
            .httpClient(unauthorizedHttpClient)
            .baseUrl(baseUrl)
            .build()
    }

    @Suppress("UnusedPrivateProperty")
    private val ktorfit by lazy {
        Ktorfit
            .Builder()
            .httpClient(authorizedHttpClient)
            .baseUrl(baseUrl)
            .build()
    }

    val auth by lazy { ktorfitNoAuth.createAuthApi() }
}

fun TemplateNetworkClient(
    networkConfig: NetworkConfig,
    settingsStorage: SettingsStorage,
    unauthUseCase: UnauthUseCase,
): TemplateNetworkClient {
    val unauthorizedHttpClient = defaultHttpClient(
        settingsStorage = settingsStorage,
        unauthUseCase = unauthUseCase,
    ) {
        // you can add header
    }
    val authorizedHttpClient = defaultHttpClient(
        settingsStorage = settingsStorage,
        unauthUseCase = unauthUseCase,
    ) {
        defaultRequest {
            val token = runBlocking { settingsStorage.getToken() }
            header("X-Auth-Key", token)
        }
    }
    return TemplateNetworkClient(networkConfig.baseUrl, unauthorizedHttpClient, authorizedHttpClient)
}

private fun defaultHttpClient(
    settingsStorage: SettingsStorage,
    unauthUseCase: UnauthUseCase,
    json: Json = TemplateDefaultJson,
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient {
    return PlatformHttpClient {
        followRedirects = false

        install(ContentNegotiation) {
            json(json, ContentType.Application.Json)
            json(json, ContentType.Text.Any)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("Ktorfit: $message")
                }
            }

            level = LogLevel.ALL
        }

        block()

        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val header = response.headers["X-Auth-Key"]
                if (!header.isNullOrBlank()) {
                    settingsStorage.setToken(header)
                }

                if (response.status == HttpStatusCode.Unauthorized ||
                    response.status == HttpStatusCode.Found
                ) { // 302 unauthorized
                    unauthUseCase.invoke()
                }
                validateHttpResponse(response)
            }

            handleResponseException { throwable: Throwable, _: HttpRequest ->
                throw mapNetworkThrowableToDomain(throwable)
            }
        }
    }
}

val TemplateDefaultJson: Json =
    Json {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
        ignoreUnknownKeys = true
    }
