package com.github.linkav20.habits.core.uikit.components.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.backhandler.BackCallback

interface DrawerDialog {

    fun openDialog(
        dialogModel: DrawerDialogModel,
        openWithAnim: OpenDialogAnimation = OpenDialogAnimation.SLIDE_FROM_LEFT,
    )

    fun closeDialog()

    fun closeImmediately()
}

interface DrawerDialogRenderer : DrawerDialog {
    @Composable
    fun Content(modifier: Modifier)

    fun registerBackCallback(backCallback: BackCallback)
    fun unRegisterBackCallback(backCallback: BackCallback)
}

enum class OpenDialogAnimation {
    SLIDE_FROM_LEFT,
    SLIDE_FROM_BOTTOM,
}
