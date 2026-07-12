package com.github.linkav20.habits.core.uikit.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitIcons
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.input_field_clear_button
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Suppress("LongParameterList")
@Composable
fun HabitInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    labelContent: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    singleLine: Boolean = true,
    errorText: String? = null,
    isLoading: Boolean = false,
    bordered: Boolean = false,
    placeholder: String = "",
    placeholderMaxLines: Int = Int.MAX_VALUE,
    leadingIcon: @Composable ((borderColor: Color) -> Unit)? = null,
    trailingIcon: @Composable ((borderColor: Color) -> Unit)? = null,
    colors: TemplateInputFieldColors = TemplateTextFieldDefaults.colors(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrectEnabled = true,
        keyboardType = KeyboardType.Unspecified,
        imeAction = ImeAction.Unspecified,
        platformImeOptions = null,
        showKeyboardOnFocus = null,
        hintLocales = null,
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        !isEnabled -> colors.disabledBorderColor
        isReadOnly -> colors.unfocusedBorderColor
        isError -> colors.errorBolderColor
        isFocused -> colors.focusedBorderColor
        else -> colors.unfocusedBorderColor
    }


    InputFieldStructure(
        textFieldContent = { textFieldColors ->
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = labelContent ?: label?.let { { Text(it) } },
                placeholder = {
                    Text(
                        text = placeholder,
                        style = HabitTheme.typography.body.copy(
                            fontStyle = FontStyle.Italic,
                        ),
                        maxLines = placeholderMaxLines,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                singleLine = singleLine,
                leadingIcon = leadingIcon?.let {
                    {
                        it.invoke(borderColor)
                    }
                },
                trailingIcon = {
                    TrailingIconWithProgress(
                        value = value,
                        borderColor = borderColor,
                        isLoading = isLoading,
                        isEnabled = isEnabled,
                        isReadOnly = isReadOnly,
                        trailingIcon = trailingIcon,
                        clearValue = {
                            onValueChange("")
                        },
                    )
                },
                isError = isError,
                enabled = isEnabled,
                readOnly = isReadOnly,
                visualTransformation = visualTransformation,
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusProperties { canFocus = !isReadOnly }
                    .clip(shape = HabitTheme.shapes.medium)
                    .then(
                        if (bordered) {
                            Modifier.border(
                                border = BorderStroke(1.dp, borderColor),
                                shape = HabitTheme.shapes.medium,
                            )
                        } else {
                            Modifier
                        },
                    ),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                textStyle = HabitTheme.typography.body.copy(
                    fontStyle = FontStyle.Italic,
                ),
            )
        },
        modifier = modifier,
        isError = isError,
        isReadOnly = isReadOnly,
        errorText = errorText,
        colors = colors,
    )
}

@Composable
private fun TrailingIconWithProgress(
    value: String,
    borderColor: Color,
    isLoading: Boolean,
    isEnabled: Boolean,
    isReadOnly: Boolean,
    trailingIcon: @Composable ((Color) -> Unit)?,
    clearValue: () -> Unit,
) {
    Row {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                strokeWidth = 3.dp,
                color = HabitTheme.colors.iconPrimary,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        trailingIcon?.invoke(borderColor) ?: run {
            if (value.isNotEmpty() && !isReadOnly && isEnabled) {
                IconButton(
                    onClick = clearValue,
                ) {
                    Icon(
                        painter = painterResource(HabitIcons.Cross_24),
                        tint = borderColor,
                        contentDescription = stringResource(Res.string.input_field_clear_button),
                    )
                }
            }
        }
    }
}

@Composable
internal fun InputFieldStructure(
    textFieldContent: @Composable ColumnScope.(TextFieldColors) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    errorText: String? = null,
    colors: TemplateInputFieldColors = TemplateTextFieldDefaults.colors(),
) {
    Column(
        modifier = modifier,
    ) {
        textFieldContent(colors.asTextFieldColors(isReadOnly = isReadOnly))

        if (isError && !errorText.isNullOrEmpty()) {
            Text(
                text = errorText,
                color = colors.errorLabelColor,
                style = HabitTheme.typography.body,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
