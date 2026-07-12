package com.github.linkav20.habits.core.uikit.components.animator

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.materialPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.essenty.backhandler.BackHandler

@OptIn(ExperimentalDecomposeApi::class)
actual fun <C : Any, T : Any> templateBackAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit,
): StackAnimation<C, T>? =
    predictiveBackAnimation(
    backHandler = backHandler,
    fallbackAnimation = stackAnimation(fade() + scale()),
    selector = { backEvent, _, _ ->
        materialPredictiveBackAnimatable(backEvent)
    },
    onBack = onBack,
)

@OptIn(ExperimentalDecomposeApi::class)
actual fun <C : Any, T : Any> templateBottomTabsAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit,
): StackAnimation<C, T> =
    predictiveBackAnimation(
    backHandler = backHandler,
    fallbackAnimation = stackAnimation(fade()),
    selector = { backEvent, _, _ ->
        materialPredictiveBackAnimatable(backEvent)
    },
    onBack = onBack,
)
