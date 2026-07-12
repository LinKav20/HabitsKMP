package com.github.linkav20.habits.common.model

public sealed class NetworkException : Exception() {

    public data object Unknown : NetworkException()

    public data class Server(
        val code: Int,
    ) : NetworkException()

    public data class Client(
        val code: Int,
    ) : NetworkException()

    public data object Timeout : NetworkException()

    public data object Connection : NetworkException()

    public data object UnknownHostException : NetworkException()

    public companion object {
        public const val NOT_FOUND_CODE: Int = 404
    }
}
