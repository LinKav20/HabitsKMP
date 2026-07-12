package com.github.linkav20.habits.core.uikit.components.inputphone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.github.linkav20.habits.common.model.Country
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.input.TemplateInputFieldColors
import com.github.linkav20.habits.core.uikit.components.input.TemplateTextFieldDefaults
import com.github.linkav20.habits.core.uikit.resources.Res
import com.github.linkav20.habits.core.uikit.resources.ic_24_cross
import com.github.linkav20.habits.core.uikit.resources.input_field_clear_button
import com.github.linkav20.habits.core.uikit.resources.phone_input_field_country
import com.github.linkav20.habits.core.uikit.resources.phone_input_field_drop_down_arrow

private val FLAG_SIZE = 24.dp

@Suppress("LongParameterList", "LongMethod")
@Composable
fun TemplateInputPhone(
    value: String,
    selectedCountry: Country,
    availableCountries: ImmutableList<Country>,
    onValueChange: (String) -> Unit,
    onCountyClick: () -> Unit,
    onSelectCounty: (Country) -> Unit,
    onDismissCountyDropDownMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    label: String? = null,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    isReadOnly: Boolean = false,
    isCountyDropDownMenuExpanded: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TemplateInputFieldColors = TemplateTextFieldDefaults.colors(),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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

    Column(modifier = modifier) {
        Row(
            modifier = getCountryModifier(
                borderColor = borderColor,
                bgColor = colors.disabledBackgroundColor,
                isEnabled = isEnabled,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CountyContent(
                color = borderColor,
                isEnabled = isEnabled,
                selectedCountry = selectedCountry,
                onClick = onCountyClick,
            )
            CountriesDropDownMenu(
                expanded = isCountyDropDownMenuExpanded,
                onItemClick = onSelectCounty,
                onDismissRequest = onDismissCountyDropDownMenuClick,
                availableCountries = availableCountries,
            )
            TextField(
                value = value,
                onValueChange = {
                    if (it.isNotBlank() && it.last().isDigit()) {
                        if (selectedCountry.mask.isNotEmpty()) {
                            val count = getSpecialSymbolsInMaskCount(
                                mask = selectedCountry.mask,
                                maskSymbol = selectedCountry.maskSymbol,
                            )
                            if (it.length > count) {
                                onValueChange(it.take(count))
                            } else {
                                onValueChange(it)
                            }
                        } else {
                            onValueChange(it)
                        }
                    } else {
                        onValueChange(it)
                    }
                },
                label = label?.let {
                    {
                        Text(
                            text = it,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = selectedCountry.mask,
                        style = HabitTheme.typography.body,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                singleLine = true,
                trailingIcon = trailingIcon ?: {
                    BaseTrailingItem(
                        value = value,
                        onValueChange = onValueChange,
                        borderColor = borderColor,
                        isEnabled = isEnabled,
                        isReadOnly = isReadOnly,
                    )
                },
                isError = isError,
                enabled = isEnabled,
                readOnly = isReadOnly,
                visualTransformation = getPhoneInputVisualTransform(selectedCountry),
                colors = colors.asTextFieldColors(isReadOnly = isReadOnly),
                modifier = getPhoneTextFieldModifier(
                    isReadOnly = isReadOnly,
                    borderColor = borderColor,
                ).fillMaxWidth(),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                textStyle = HabitTheme.typography.body,
            )
        }
        ErrorContent(
            errorText = errorText,
            color = colors.errorLabelColor,
            isError = isError,
        )
    }
}

@Composable
private fun Modifier.addClickable(
    isEnabled: Boolean,
    onClick: () -> Unit,
) = if (isEnabled) {
    this.clickable { onClick() }
} else {
    this
}

@Composable
private fun CountriesDropDownMenu(
    expanded: Boolean,
    onItemClick: (Country) -> Unit,
    onDismissRequest: () -> Unit,
    availableCountries: ImmutableList<Country>,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = HabitTheme.colors.surfaceContainer,
    ) {
        availableCountries.forEach {
            DropdownMenuItem(
                leadingIcon = {
                    AsyncFlag(imageUrl = it.image)
                },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = it.name,
                            style = HabitTheme.typography.body,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(
                            text = it.phoneCode,
                            color = HabitTheme.colors.textPrimary,
                            style = HabitTheme.typography.body,
                        )
                    }
                },
                onClick = { onItemClick(it) },
            )
        }
    }
}

@Composable
private fun AsyncFlag(imageUrl: String) {
    val painter = rememberAsyncImagePainter(imageUrl)
    val state by painter.state.collectAsState()
    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading,
            -> {
            CircularProgressIndicator(
                modifier = Modifier.size(FLAG_SIZE),
                color = HabitTheme.colors.surfaceContainer,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round,
            )
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = stringResource(Res.string.phone_input_field_country),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(FLAG_SIZE),
            )
        }

        is AsyncImagePainter.State.Error -> {
            val flag = TODO()
            Image(
                painter = painterResource(flag),
                contentDescription = stringResource(Res.string.phone_input_field_country),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(FLAG_SIZE),
            )
        }
    }
}

@Composable
private fun ErrorContent(
    errorText: String?,
    color: Color,
    isError: Boolean,
) {
    if (isError && !errorText.isNullOrEmpty()) {
        Text(
            text = errorText,
            color = color,
            style = HabitTheme.typography.body,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun CountyContent(
    selectedCountry: Country,
    color: Color,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .addClickable(isEnabled = isEnabled, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
    ) {
        AsyncFlag(imageUrl = selectedCountry.image)
        Icon(
            painter = painterResource(Res.drawable.ic_24_cross),
            tint = color,
            contentDescription = stringResource(Res.string.phone_input_field_drop_down_arrow),
        )
    }
}

@Composable
private fun BaseTrailingItem(
    value: String,
    onValueChange: (String) -> Unit,
    borderColor: Color,
    isEnabled: Boolean,
    isReadOnly: Boolean,
) {
    if (value.isNotEmpty() && !isReadOnly && isEnabled) {
        IconButton(
            onClick = {
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
}

@Composable
private fun getCountryModifier(
    borderColor: Color,
    bgColor: Color,
    isEnabled: Boolean,
) = Modifier
    .clip(shape = HabitTheme.shapes.medium)
    .border(
        border = BorderStroke(1.dp, borderColor),
        shape = HabitTheme.shapes.medium,
    ).background(
        if (isEnabled) {
            HabitTheme.colors.surfaceContainer
        } else {
            bgColor
        },
    )

@Composable
private fun getPhoneTextFieldModifier(
    isReadOnly: Boolean,
    borderColor: Color,
) = Modifier
    .focusProperties { canFocus = !isReadOnly }
    .clip(
        shape = HabitTheme.shapes.medium.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp),
        ),
    ).border(
        border = BorderStroke(1.dp, borderColor),
        shape = HabitTheme.shapes.medium.copy(
            topStart = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp),
        ),
    )

private fun getSpecialSymbolsInMaskCount(
    mask: String,
    maskSymbol: Char,
) = mask.count { it == maskSymbol }

private fun getPhoneInputVisualTransform(country: Country) =
    if (country.mask.isNotEmpty()) {
        MaskVisualTransformation(
            mask = country.mask,
            maskSymbol = country.maskSymbol,
            phoneCode = country.phoneCode,
        )
    } else {
        VisualTransformation.None
    }

class MaskVisualTransformation(
    private val mask: String,
    private val maskSymbol: Char,
    private val phoneCode: String,
    private val placeholderChar: Char = '0',
    private val placeholderColor: Color = Color.Gray,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val builder = AnnotatedString.Builder()
        val digits = text.text.filter { it.isDigit() }
        val filledSymbols = digits.length
        var textIndex = 0

        mask.forEachIndexed { index, maskChar ->
            when {
                index < phoneCode.length -> builder.append(mask[index])
                maskChar == maskSymbol -> {
                    if (textIndex < filledSymbols) {
                        builder.append(digits[textIndex])
                        textIndex++
                    } else {
                        builder.pushStyle(SpanStyle(color = placeholderColor))
                        builder.append(placeholderChar)
                        builder.pop()
                    }
                }

                else -> {
                    val hasInputBefore = mask
                        .take(index)
                        .count { it == maskSymbol } <= filledSymbols
                    if (hasInputBefore && filledSymbols > 0) {
                        builder.append(maskChar)
                    } else {
                        builder.pushStyle(SpanStyle(color = placeholderColor))
                        builder.append(maskChar)
                        builder.pop()
                    }
                }
            }
        }

        return TransformedText(
            builder.toAnnotatedString(),
            offsetMapping(text),
        )
    }

    private fun offsetMapping(text: AnnotatedString) =
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var maskOffset = phoneCode.length
                var count = 0
                while (maskOffset < mask.length && count < offset) {
                    if (mask[maskOffset] == maskSymbol) count++
                    maskOffset++
                }
                return maskOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= phoneCode.length) return 0
                var count = 0
                for (i in phoneCode.length until offset.coerceAtMost(mask.length)) {
                    if (mask[i] == maskSymbol) count++
                }
                return count.coerceAtMost(text.length)
            }
        }
}

val CountrySaver: Saver<MutableState<Country>, String> = Saver(
    save = { Json.encodeToString(it.value) },
    restore = { mutableStateOf(Json.decodeFromString<Country>(it)) },
)
