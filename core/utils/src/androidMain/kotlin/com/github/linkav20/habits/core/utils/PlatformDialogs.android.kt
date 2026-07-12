package com.github.linkav20.habits.core.utils

import android.app.AlertDialog
import com.github.linkav20.habits.core.utils.resources.Res
import com.github.linkav20.habits.core.utils.resources.okay_button
import com.github.linkav20.habits.core.utils.resources.restart_app_subtitle
import com.github.linkav20.habits.core.utils.resources.restart_app_title
import org.jetbrains.compose.resources.getString

public actual suspend fun showNeedToRestartDialog() {
    AlertDialog.Builder(ActivityStackFactory.getContext())
        .setTitle(getString(Res.string.restart_app_title))
        .setMessage(getString(Res.string.restart_app_subtitle))
        .setPositiveButton(getString(Res.string.okay_button)) { _, _ -> }
        .show()
}

public actual suspend fun showLanguageSettingDialog() {
    throw NotImplementedError("showLanguageSettingDialog is not implemented for this platform yet")
}
