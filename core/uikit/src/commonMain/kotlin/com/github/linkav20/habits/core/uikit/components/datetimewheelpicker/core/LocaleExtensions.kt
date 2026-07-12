package com.github.linkav20.habits.core.uikit.components.datetimewheelpicker.core

import androidx.compose.ui.text.intl.Locale

private val CJK_LANGUAGES = listOf("zh", "ja", "ko")

val Locale.isCjkLanguage: Boolean
  get() = language in CJK_LANGUAGES
