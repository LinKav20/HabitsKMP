package com.github.linkav20.habits.core.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import splitties.init.directBootCtx

public actual fun copyToClipboard(text: String) {
    val clipboard = directBootCtx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("", text)
    clipboard.setPrimaryClip(clip)
}
