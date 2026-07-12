package com.github.linkav20.habits.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.github.linkav20.habits.compose.presentation.root.RootComponent
import com.github.linkav20.habits.compose.presentation.root.RootNavigationChild
import org.koin.compose.koinInject
import com.github.linkav20.habits.auth.api.AuthContent
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.animator.templateBackAnimation
import com.github.linkav20.habits.core.uikit.components.crossfade.TemplateCrossfade
import com.github.linkav20.habits.core.uikit.components.drawer.LocalDrawerDialogProvider
import com.github.linkav20.habits.core.uikit.components.drawer.createDialogDrawer
import com.github.linkav20.habits.core.uikit.components.input.swipeToDismissKeyboard
import com.github.linkav20.habits.core.uikit.components.snackbar.TemplateSnackBarHost
import com.github.linkav20.habits.core.uikit.components.snackbar.rememberTemplateSnackBarHostState
import com.github.linkav20.habits.core.uikit.statusbar.statusBar
import com.github.linkav20.habits.datastore.datasource.ThemePreference
import com.github.linkav20.habits.debugmenu.api.DebugMenuContent
import com.gitub.linkav20.habits.main.api.MainNavContent

@Composable
fun MainContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val drawerDialog = createDialogDrawer(component.backHandler)
    val theme by component.theme.collectAsState()

    val isDark = when (theme) {
        ThemePreference.DARK -> true
        ThemePreference.LIGHT -> false
        ThemePreference.SYSTEM -> isSystemInDarkTheme()
    }

    TemplateCrossfade(state = isDark) { isDarkTheme ->
        HabitTheme(isDarkMode = isDarkTheme) {
            statusBar.SetStatusBarLightMode(isDarkStatusBarColor = !isDarkTheme)
            CompositionLocalProvider(
                LocalDrawerDialogProvider provides drawerDialog,
            ) {
                val templateSnackBarHostState = rememberTemplateSnackBarHostState()
                Scaffold(
                    modifier = modifier.swipeToDismissKeyboard(),
                    snackbarHost = { TemplateSnackBarHost(hostState = templateSnackBarHostState) },
                ) {
                    LaunchedEffect(Unit) {
                        component.snackBarFlow.collect { data ->
                            templateSnackBarHostState.showSnackBar(data)
                        }
                    }
                    Children(
                        stack = component.stack,
                        modifier = Modifier.fillMaxSize(),
                        animation = templateBackAnimation(
                            backHandler = component.backHandler,
                            onBack = component::onBackClicked,
                        ),
                    ) {
                        when (val instance: RootNavigationChild = it.instance) {
                            is RootNavigationChild.Auth -> {
                                koinInject<AuthContent>()
                                    .Content(instance.component, Modifier)
                            }

                            is RootNavigationChild.Main -> {
                                koinInject<MainNavContent>()
                                    .Content(instance.component, Modifier)
                            }

                            is RootNavigationChild.DebugMenu -> {
                                koinInject<DebugMenuContent>()
                                    .Content(instance.component, Modifier)
                            }
                        }
                    }
                }

                drawerDialog.Content(
                    modifier = Modifier,
                )
            }
        }
    }
}
