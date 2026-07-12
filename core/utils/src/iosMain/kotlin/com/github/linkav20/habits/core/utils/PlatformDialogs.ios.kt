package com.github.linkav20.habits.core.utils

import com.github.linkav20.habits.core.utils.resources.Res
import com.github.linkav20.habits.core.utils.resources.cancel_button
import com.github.linkav20.habits.core.utils.resources.change_language_text
import com.github.linkav20.habits.core.utils.resources.change_language_title
import com.github.linkav20.habits.core.utils.resources.edit_button
import com.github.linkav20.habits.core.utils.resources.okay_button
import com.github.linkav20.habits.core.utils.resources.restart_app_subtitle
import com.github.linkav20.habits.core.utils.resources.restart_app_title
import org.jetbrains.compose.resources.getString
import platform.Foundation.NSURL
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import kotlin.system.exitProcess

public actual suspend fun showNeedToRestartDialog() {
    val alert = UIAlertController.alertControllerWithTitle(
        title = getString(Res.string.restart_app_title),
        message = getString(Res.string.restart_app_subtitle),
        preferredStyle = UIAlertControllerStyleAlert,
    )

    val alertAction = UIAlertAction.actionWithTitle(
        title = getString(Res.string.okay_button),
        style = UIAlertActionStyleDefault,
        handler = { exitProcess(0) },
    )
    alert.addAction(alertAction)

    UIApplication.sharedApplication.keyWindow?.rootViewController
        ?.presentViewController(alert, animated = true, completion = null)
}

public actual suspend fun showLanguageSettingDialog() {
    val alert = UIAlertController.alertControllerWithTitle(
        title = getString(Res.string.change_language_title),
        message = getString(Res.string.change_language_text),
        preferredStyle = UIAlertControllerStyleAlert,
    )
    val cancelAction = UIAlertAction.actionWithTitle(
        title = getString(Res.string.cancel_button),
        style = UIAlertActionStyleCancel,
        handler = {},
    )

    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return
    val sharedApplication = UIApplication.sharedApplication

    val editAction = UIAlertAction.actionWithTitle(
        title = getString(Res.string.edit_button),
        style = UIAlertActionStyleDefault,
        handler = {
            if (sharedApplication.canOpenURL(url)) {
                sharedApplication.openURL(
                    url = url,
                    options = emptyMap<Any?, Any?>(),
                    completionHandler = null,
                )
            }
        },
    )

    alert.addAction(cancelAction)
    alert.addAction(editAction)

    UIApplication.sharedApplication.keyWindow?.rootViewController
        ?.presentViewController(alert, animated = true, completion = null)
}
