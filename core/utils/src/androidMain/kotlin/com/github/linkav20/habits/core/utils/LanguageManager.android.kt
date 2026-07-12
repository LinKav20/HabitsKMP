package com.github.linkav20.habits.core.utils

import android.app.LocaleManager
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import splitties.init.directBootCtx

public actual fun getSystemLanguage(): Language {
    val config = Resources.getSystem().configuration
    val locale = config.locales.get(0).language
    return when (locale) {
        "ru" -> Language.Russian
        "en" -> Language.English
        else -> Language.Russian
    }
}

public actual fun changeLanguage(language: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager = directBootCtx.getSystemService(LocaleManager::class.java)
        localeManager.applicationLocales = LocaleList.forLanguageTags(language)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }
}
