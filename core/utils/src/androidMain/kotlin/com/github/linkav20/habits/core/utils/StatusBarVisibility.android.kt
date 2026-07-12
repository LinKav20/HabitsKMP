package com.github.linkav20.habits.core.utils

import android.view.View
import androidx.activity.ComponentActivity

@Suppress("ReturnCount")
public actual fun setStatusBarVisibility(isVisible: Boolean) {
    val activity = ActivityStackFactory.getContext() as? ComponentActivity ?: return
    val window = activity.window ?: return
    @Suppress("DEPRECATION")
    window.decorView.systemUiVisibility = if (isVisible) {
        View.SYSTEM_UI_FLAG_VISIBLE
    } else {
        (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}
