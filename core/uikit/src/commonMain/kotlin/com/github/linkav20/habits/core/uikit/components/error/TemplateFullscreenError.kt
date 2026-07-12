@file:Suppress("LambdaParameterEventTrailing", "ComposableParamOrder")

package com.github.linkav20.habits.core.uikit.components.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import com.github.linkav20.habits.common.model.NetworkException
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.button.HabitButton
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.error_connection_subtitle
import com.github.linkav20.habits.core.uikit.resources.error_connection_title
import com.github.linkav20.habits.core.uikit.resources.error_not_found_subtitle
import com.github.linkav20.habits.core.uikit.resources.error_not_found_title
import com.github.linkav20.habits.core.uikit.resources.error_restart_button
import com.github.linkav20.habits.core.uikit.resources.error_return_to_main_button
import com.github.linkav20.habits.core.uikit.resources.error_server_subtitle
import com.github.linkav20.habits.core.uikit.resources.error_server_title
import com.github.linkav20.habits.core.uikit.resources.error_timeout_subtitle
import com.github.linkav20.habits.core.uikit.resources.error_timeout_title
import com.github.linkav20.habits.core.uikit.resources.error_unknown_subtitle
import com.github.linkav20.habits.core.uikit.resources.error_unknown_title

@Composable
fun TemplateFullscreenError(
    exception: Throwable,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (exception is NetworkException) {
        TemplateFullscreenErrorNetworkException(
            exception = exception,
            modifier = modifier,
            onActionClick = onActionClick,
        )
    } else {
        TemplateFullscreenThrowable(
            message = exception.message,
            onActionClick = onActionClick,
            modifier = modifier,
        )
    }
}

@Composable
fun TemplateFullscreenErrorNetworkException(
    exception: NetworkException,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = getTitle(exception)
    val subtitle = getSubtitle(exception)
    val buttonText = getButtonText(exception)

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            // You can add image if its needed
            Text(
                text = title,
                style = HabitTheme.typography.bodyAccent,
                color = HabitTheme.colors.textAccentPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = HabitTheme.typography.body,
                textAlign = TextAlign.Center,
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            HabitButton(
                modifier = Modifier.fillMaxWidth(),
                title = buttonText,
                onClick = onActionClick,
            )
        }
    }
}

@Composable
fun TemplateFullscreenThrowable(
    message: String? = null,
    title: String? = null,
    buttonText: String? = null,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = title ?: stringResource(Res.string.error_unknown_title),
                style = HabitTheme.typography.bodyAccent,
                color = HabitTheme.colors.textAccentPrimary,
                textAlign = TextAlign.Center,
            )
        }

        if (!message.isNullOrEmpty()) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    style = HabitTheme.typography.body,
                    textAlign = TextAlign.Center,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            HabitButton(
                modifier = Modifier.fillMaxWidth(),
                title = buttonText ?: stringResource(Res.string.error_restart_button),
                onClick = onActionClick,
            )
        }
    }
}

@Composable
private fun getTitle(exception: NetworkException) =
    when (exception) {
        NetworkException.Timeout -> stringResource(Res.string.error_timeout_title)
        is NetworkException.Client -> {
            if (exception.code == NetworkException.NOT_FOUND_CODE) {
                stringResource(Res.string.error_not_found_title)
            } else {
                stringResource(Res.string.error_unknown_title)
            }
        }

        is NetworkException.Server -> stringResource(Res.string.error_server_title)
        NetworkException.Connection -> stringResource(Res.string.error_connection_title)
        else -> stringResource(Res.string.error_unknown_title)
    }

@Composable
private fun getSubtitle(exception: NetworkException) =
    when (exception) {
        NetworkException.Timeout -> stringResource(Res.string.error_timeout_subtitle)
        is NetworkException.Client -> {
            if (exception.code == NetworkException.NOT_FOUND_CODE) {
                stringResource(Res.string.error_not_found_subtitle)
            } else {
                stringResource(Res.string.error_unknown_subtitle)
            }
        }

        is NetworkException.Server -> stringResource(Res.string.error_server_subtitle)
        NetworkException.Connection -> stringResource(Res.string.error_connection_subtitle)
        else -> stringResource(Res.string.error_unknown_subtitle)
    }

@Composable
private fun getButtonText(exception: NetworkException) =
    when (exception) {
        is NetworkException.Client -> {
            if (exception.code == NetworkException.NOT_FOUND_CODE) {
                stringResource(Res.string.error_return_to_main_button)
            } else {
                stringResource(Res.string.error_restart_button)
            }
        }

        else -> stringResource(Res.string.error_restart_button)
    }
