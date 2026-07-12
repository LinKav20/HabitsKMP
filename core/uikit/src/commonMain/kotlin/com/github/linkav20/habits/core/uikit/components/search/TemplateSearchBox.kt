package com.github.linkav20.habits.core.uikit.components.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.input.InputFieldStructure
import com.github.linkav20.habits.core.uikit.components.input.TemplateInputFieldColors
import com.github.linkav20.habits.core.uikit.components.input.TemplateTextFieldDefaults
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.ic_24_cross
import com.github.linkav20.habits.core.uikit.resources.ic_24_search
import com.github.linkav20.habits.core.uikit.resources.input_field_clear_button
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TemplateSearchBox(
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    colors: TemplateInputFieldColors = TemplateTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        !isEnabled -> colors.disabledBorderColor
        isError -> colors.errorBolderColor
        isFocused -> colors.focusedBorderColor
        else -> colors.unfocusedBorderColor
    }

    InputFieldStructure(
        textFieldContent = { textFieldColors ->
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onValueChange(it)
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        style = HabitTheme.typography.body,
                    )
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_24_search),
                        tint = borderColor,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty() && isEnabled) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                onValueChange("")
                            },
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_24_cross),
                                tint = borderColor,
                                contentDescription = stringResource(Res.string.input_field_clear_button),
                            )
                        }
                    }
                },
                isError = isError,
                enabled = isEnabled,
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = HabitTheme.shapes.medium)
                    .border(
                        border = BorderStroke(1.dp, borderColor),
                        shape = HabitTheme.shapes.medium,
                    ),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                textStyle = HabitTheme.typography.body,
            )
        },
        modifier = modifier,
        isError = isError,
        errorText = errorText,
        colors = colors,
    )
}
