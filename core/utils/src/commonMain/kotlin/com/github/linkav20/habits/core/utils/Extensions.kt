package com.github.linkav20.habits.core.utils

import kotlin.coroutines.cancellation.CancellationException

private val emailRegex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$".toRegex()
private val telegramRegex = "^@[a-zA-Z0-9_]{5,32}$".toRegex()

public fun String.isValidOrBlankEmail(): Boolean {
    if (this.isBlank()) return true
    return emailRegex.matches(this)
}

public fun String.isValidOrBlankTelegram(): Boolean {
    if (this.isBlank()) return true
    return telegramRegex.matches(this)
}

private fun extractQueryParams(link: String): Map<String, String> {
    return link
        .substringAfter('?', "")
        .split('&')
        .mapNotNull { param ->
            val parts = param.split('=', limit = 2)
            if (parts.size == 2) parts[0] to parts[1] else null
        }.toMap()
}

public fun getQueryParam(
    link: String,
    key: String,
): String? {
    return extractQueryParams(link)[key]
}

public inline fun <T> Result<T>.onFailureActiveCoroutine(action: (exception: Throwable) -> Unit): Result<T> {
    return onFailure {
        if (it is CancellationException) {
            throw it
        }
        action(it)
    }
}

public fun String.getExtensionFromUrl(): String? {
    val clean = this.substringBefore('?').substringBefore('#')
    val filename = clean.substringAfterLast('/', "")
    if (!filename.contains('.')) return null
    val ext = filename.substringAfterLast('.', "").lowercase()
    return ext.ifBlank { null }
}
