package com.github.linkav20.habits.profile.impl.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.topbar.HabitCenterTopBar
import com.github.linkav20.habits.core.utils.currentEnvironment
import com.github.linkav20.habits.profile.api.profile.ProfileComponent
import com.github.linkav20.habits.profile.api.profile.ProfileContent
import com.github.linkav20.habits.profile.impl.resources.Res
import com.github.linkav20.habits.profile.impl.resources.profile_all_habits
import com.github.linkav20.habits.profile.impl.resources.profile_app_language
import com.github.linkav20.habits.profile.impl.resources.profile_app_theme
import com.github.linkav20.habits.profile.impl.resources.profile_debug_menu
import com.github.linkav20.habits.profile.impl.resources.profile_mood_diary
import com.github.linkav20.habits.profile.impl.resources.profile_production
import com.github.linkav20.habits.profile.impl.resources.profile_title
import com.github.linkav20.habits.profile.impl.resources.profile_version
import com.github.linkav20.habits.profile.impl.ui.profile.elements.MenuItem
import com.github.linkav20.habits.profile.impl.ui.profile.elements.ProfileMenuBlock
import org.jetbrains.compose.resources.stringResource

internal class ProfileContentImpl : ProfileContent {
    @Composable
    override fun Content(
        component: ProfileComponent,
        modifier: Modifier,
    ) {
        ProfileScreen(
            onBackClick = component::onBackClicked,
            onAllHabitsClick = component::onAllHabitsClicked,
            onDebugMenuClick = component::onDebugMenuClicked,
            modifier = modifier,
        )
    }
}

@Composable
private fun ProfileScreen(
    onBackClick: () -> Unit,
    onAllHabitsClick: () -> Unit,
    onDebugMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = HabitTheme.colors.background,
        topBar = {
            HabitCenterTopBar(
                title = stringResource(Res.string.profile_title),
                containerColor = HabitTheme.colors.background,
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            ProfileFooter()
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ProfileMenuBlock(
                items = listOf(
                    MenuItem(
                        title = stringResource(Res.string.profile_all_habits),
                        action = onAllHabitsClick,
                    ),
                    MenuItem(
                        title = stringResource(Res.string.profile_mood_diary),
                        action = {},
                    ),
                ),
            )
            ProfileMenuBlock(
                items = buildList {
                    add(
                        MenuItem(
                            title = stringResource(Res.string.profile_app_theme),
                            action = {},
                        ),
                    )
                    add(
                        MenuItem(
                            title = stringResource(Res.string.profile_app_language),
                            action = {},
                        ),
                    )
                    if (currentEnvironment.isDebug) {
                        add(
                            MenuItem(
                                title = stringResource(Res.string.profile_debug_menu),
                                action = onDebugMenuClick,
                            ),
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun ProfileFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.profile_production),
            style = HabitTheme.typography.textSmall,
            color = HabitTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(Res.string.profile_version, currentEnvironment.appVersion),
            style = HabitTheme.typography.textSmall,
            color = HabitTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
