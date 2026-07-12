package com.github.linkav20.habits.debugmenu.impl.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.components.topbar.HabitTopBar
import com.github.linkav20.habits.core.utils.copyToClipboard
import com.github.linkav20.habits.core.utils.currentEnvironment
import com.github.linkav20.habits.core.utils.letString
import com.github.linkav20.habits.debugmenu.api.DebugMenuComponent
import com.github.linkav20.habits.debugmenu.api.DebugMenuContent
import com.github.linkav20.habits.debugmenu.api.DebugMenuEvent
import com.github.linkav20.habits.debugmenu.impl.resources.Res
import com.github.linkav20.habits.debugmenu.impl.resources.debug_menu_title
import com.github.linkav20.habits.debugmenu.impl.resources.device_id_title
import com.github.linkav20.habits.debugmenu.impl.resources.os_version_title
import com.github.linkav20.habits.debugmenu.impl.resources.prod_value
import com.github.linkav20.habits.debugmenu.impl.resources.reset_token_config_title
import com.github.linkav20.habits.debugmenu.impl.resources.stage_value
import com.github.linkav20.habits.debugmenu.impl.resources.token_title

internal class DebugMenuContentImpl : DebugMenuContent {
    @Composable
    override fun Content(
        component: DebugMenuComponent,
        modifier: Modifier,
    ) {
        val state by component.model.collectAsState()

        Scaffold(
            topBar = {
                HabitTopBar(
                    title = stringResource(Res.string.debug_menu_title),
                    onBackClick = { component.handleAction(DebugMenuEvent.OnBackClick) },
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                EnvironmentBlock(
                    isStage = true,
                    isBlockSelected = state.isStage,
                    token = state.stageToken,
                    onClearToken = { component.handleAction(DebugMenuEvent.ResetStageToken) },
                    onChangeEnvironment = { component.handleAction(DebugMenuEvent.ChangeIsStage(true)) },
                )
                EnvironmentBlock(
                    isStage = false,
                    isBlockSelected = !state.isStage,
                    token = state.prodToken,
                    onClearToken = { component.handleAction(DebugMenuEvent.ResetProdToken) },
                    onChangeEnvironment = { component.handleAction(DebugMenuEvent.ChangeIsStage(false)) },
                )
                MenuTextRow(
                    title = stringResource(Res.string.os_version_title),
                    value = currentEnvironment.osVersion,
                )
                MenuTextRow(
                    title = stringResource(Res.string.device_id_title),
                    value = currentEnvironment.deviceId,
                )
            }
        }
    }
}

@Composable
private fun EnvironmentBlock(
    isStage: Boolean,
    isBlockSelected: Boolean,
    token: String?,
    onClearToken: () -> Unit,
    onChangeEnvironment: () -> Unit,
) = Column {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (isStage) {
                stringResource(Res.string.stage_value)
            } else {
                stringResource(Res.string.prod_value)
            },
            style = HabitTheme.typography.bodyAccent,
        )
        RadioButton(
            selected = isBlockSelected,
            onClick = { onChangeEnvironment() },
        )
    }
    letString(token) {
        MenuTextRow(
            title = stringResource(Res.string.token_title),
            value = it,
            isCopy = true,
            showDivider = false,
        )
        Spacer(modifier = Modifier.height(8.dp))
        HabitButton(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.reset_token_config_title),
            onClick = onClearToken,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    HorizontalDivider()
}

@Composable
private fun MenuTextRow(
    title: String,
    value: String,
    isCopy: Boolean = false,
    showDivider: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                withStyle(style = HabitTheme.typography.bodyAccent.toSpanStyle()) {
                    append(title)
                }
                withStyle(style = HabitTheme.typography.body.toSpanStyle()) {
                    append(value)
                }
            },
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        if (isCopy) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 8.dp)
                    .clickable {
                        copyToClipboard(value)
                    },
                painter = painterResource(HabitIcons.Copy_24),
                tint = HabitTheme.colors.iconAccentPrimary,
                contentDescription = null,
            )
        }
    }
    if (showDivider) {
        HorizontalDivider()
    }
}
