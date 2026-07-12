package com.github.linkav20.habits.core.uikit.components.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.close
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun createDialogDrawer(backHandler: BackHandler): DrawerDialogRenderer {
    return remember {
        DrawerDialogImpl(backHandler)
    }
}

internal class DrawerDialogImpl(
    @Stable
    private val backHandler: BackHandler,
) : DrawerDialogRenderer {

    private val dialogState = mutableStateOf<DrawerDialogModel?>(null)

    private val transitionState = MutableTransitionState(false)
    private val animationTypeState = mutableStateOf(OpenDialogAnimation.SLIDE_FROM_LEFT)

    private val backCallback = BackCallback(
        isEnabled = false,
        priority = Int.MAX_VALUE,
    ) {
        closeDialog()
    }

    @Suppress("NonSkippableComposable")
    @Composable
    override fun Content(modifier: Modifier) {
        if (
            !transitionState.targetState &&
            !transitionState.currentState
        ) {
            clearModel()
        }

        val animationType = animationTypeState.value

        AnimatedVisibility(
            visibleState = transitionState,
            enter = when (animationType) {
                OpenDialogAnimation.SLIDE_FROM_LEFT -> slideIn(
                    initialOffset = {
                        IntOffset(
                            x = -it.width,
                            y = 0,
                        )
                    },
                )

                OpenDialogAnimation.SLIDE_FROM_BOTTOM -> slideIn(
                    initialOffset = {
                        IntOffset(
                            x = 0,
                            y = it.height,
                        )
                    },
                )
            },
            exit = when (animationType) {
                OpenDialogAnimation.SLIDE_FROM_LEFT -> slideOut(
                    targetOffset = {
                        IntOffset(
                            x = -it.width,
                            y = 0,
                        )
                    },
                )

                OpenDialogAnimation.SLIDE_FROM_BOTTOM -> slideOut(
                    targetOffset = {
                        IntOffset(
                            x = 0,
                            y = it.height,
                        )
                    },
                )
            },
        ) {
            val dialogModel = dialogState.value

            if (dialogModel != null) {
                Content(
                    dialogModel = dialogModel,
                    modifier = modifier
                        .fillMaxSize(),
                )
            }
        }
    }

    override fun registerBackCallback(backCallback: BackCallback) {
        backHandler.register(backCallback)
    }

    override fun unRegisterBackCallback(backCallback: BackCallback) {
        if (backHandler.isRegistered(backCallback)) {
            backHandler.unregister(backCallback)
        }
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content(
        dialogModel: DrawerDialogModel,
        modifier: Modifier = Modifier,
    ) {
        val fm = LocalFocusManager.current

        DisposableEffect(Unit) {
            fm.clearFocus()
            backHandler.register(backCallback)
            onDispose {
                backHandler.unregister(backCallback)
            }
        }

        Scaffold(
            modifier = modifier.background(HabitTheme.colors.surfaceContainer).clickable(
                interactionSource = null,
                indication = null,
                onClick = {},
            ).windowInsetsPadding(WindowInsets.systemBars),
            containerColor = HabitTheme.colors.surfaceContainer,
            topBar = {
                Row(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.weight(1f),
                    ) {
                        CompositionLocalProvider(
                            LocalTextStyle provides HabitTheme.typography.bodyAccent.copy(hyphens = Hyphens.Auto),
                        ) {
                            dialogModel.titleContent()
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    IconButton(
                        onClick = {
                            closeDialog()
                        },
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(24.dp)
                            .align(Alignment.Top),
                    ) {
                        Icon(
                            painter = painterResource(HabitIcons.Success_24),
                            tint = HabitTheme.colors.iconPrimary,
                            contentDescription = stringResource(Res.string.close),
                        )
                    }
                }
            },
            bottomBar = { Box(modifier = Modifier.padding(16.dp)) { dialogModel.bottomAreaContent() } },
        ) { paddings ->
            Column(
                modifier = Modifier
                    .padding(paddings)
                    .padding(16.dp)
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical,
                    ),
            ) {
                dialogModel.mainAreaContent()
            }
        }
    }

    override fun openDialog(
        dialogModel: DrawerDialogModel,
        openWithAnim: OpenDialogAnimation,
    ) {
        this.dialogState.value = dialogModel
        this.animationTypeState.value = openWithAnim
        backCallback.isEnabled = true
        transitionState.targetState = true
    }

    override fun closeDialog() {
        backCallback.isEnabled = false
        transitionState.targetState = false
    }

    override fun closeImmediately() {
        clearModel()
        backCallback.isEnabled = false
        transitionState.targetState = false
    }

    private fun clearModel() {
        this.dialogState.value = null
        this.animationTypeState.value = OpenDialogAnimation.SLIDE_FROM_LEFT
    }
}
