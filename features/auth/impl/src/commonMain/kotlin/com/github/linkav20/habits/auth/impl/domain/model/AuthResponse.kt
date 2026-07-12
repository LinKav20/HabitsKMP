package com.github.linkav20.habits.auth.impl.domain.model

data class Code(
    val codeData: Data?,
    val metrics: Metrics,
    val status: Status,
)

data class Data(
    val error: String?,
    val payload: Payload?,
    val result: Int?,
    val correlationId: String?,
)

data class Metrics(
    val pageCurrent: Int?,
    val pageFrom: Int?,
    val pageLast: Int?,
    val pageList: List<Int>,
    val pageTo: Int?,
    val resultCount: Int?,
    val resultLimit: Int?,
    val resultOffset: Int?,
    val resultPage: Int?,
    val resultSize: Int?,
    val timeExecution: String?,
    val timeQuery: String?,
)

data class Payload(
    val authMethod: String?,
    val captcha: String?,
    val sticker: String?,
    val ttl: Int?,
)

data class Status(
    val code: String?,
    val description: String?,
    val error: String?,
    val source: String?,
    val status: Int?,
)

data class Auth(
    val code: String?,
    val description: String?,
    val payload: com.github.linkav20.habits.auth.impl.domain.model.AuthPayload?,
    val profileUid: String?,
    val userUid: String?,
    val authToken: String?,
)

data class AuthPayload(
    val userId: String?,
)
