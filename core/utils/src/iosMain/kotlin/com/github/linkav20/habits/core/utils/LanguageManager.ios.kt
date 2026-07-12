package com.github.linkav20.habits.core.utils

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

public actual fun getSystemLanguage(): Language {
    val languageCode = NSLocale.currentLocale.languageCode
    return when (languageCode) {
        "ru" -> Language.Russian
        "en" -> Language.English
        else -> Language.Russian
    }
}

public actual fun changeLanguage(language: String) {
    throw NotImplementedError("changeLanguage is not implemented for this platform yet")
}
