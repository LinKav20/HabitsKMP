package com.github.linkav20.habits.core.utils

public expect fun getSystemLanguage(): Language
public expect fun changeLanguage(language: String)

public sealed class Language(public val iso: String) {
    public data object English : Language("en")
    public data object Russian : Language("ru")
    public data object System : Language("system")
}

public fun onSelectLanguage(language: String) {
    if (language == Language.System.iso) {
        val systemLanguage = getSystemLanguage().iso
        changeLanguage(systemLanguage)
    } else {
        changeLanguage(language)
    }
}
