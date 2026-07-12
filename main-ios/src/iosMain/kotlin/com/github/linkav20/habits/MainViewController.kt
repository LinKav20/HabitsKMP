package com.github.linkav20.habits

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import com.github.linkav20.habits.compose.MainContent
import com.github.linkav20.habits.compose.presentation.root.RootComponent

@Suppress("unused", "FunctionName")
fun CreateRootComponent(): RootComponent {
    val koinInjector = object : KoinComponent {
    }

    val rootFactory: RootComponent.Factory = koinInjector.get()

    return rootFactory.create(
        DefaultComponentContext(
            backHandler = BackDispatcherStore.dispatcher,
            lifecycle = ApplicationLifecycle(),
        ),
    )
}

@OptIn(ExperimentalDecomposeApi::class)
@Suppress("unused", "FunctionName")
fun MainViewController(rootComponent: RootComponent) =
    ComposeUIViewController {
        PredictiveBackGestureOverlay(
            backDispatcher = BackDispatcherStore.dispatcher,
            backIcon = { _, _ -> },
            modifier = Modifier.fillMaxSize(),
        ) {
            MainContent(
                component = rootComponent,
            )
        }
    }
