package com.github.linkav20.habits.core.uikit.components.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import com.github.linkav20.habits.common.model.Country
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.github.linkav20.habits.core.uikit.components.inputphone.TemplateInputPhone

@Preview
@Composable
internal fun HabitInputPhonePreview() {
    HabitTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            TemplateInputPhone(
                value = "",
                label = "Телефон",
                selectedCountry = getRussiaCountry(),
                availableCountries = listOf(getRussiaCountry()).toImmutableList(),
                onValueChange = {},
                onCountyClick = {},
                onSelectCounty = {},
                onDismissCountyDropDownMenuClick = {},
            )
            TemplateInputPhone(
                value = "+7 917",
                label = "Телефон",
                isEnabled = false,
                selectedCountry = getRussiaCountry(),
                availableCountries = listOf(getRussiaCountry()).toImmutableList(),
                onValueChange = {},
                onCountyClick = {},
                onSelectCounty = {},
                onDismissCountyDropDownMenuClick = {},
            )
            TemplateInputPhone(
                value = "",
                label = "Телефон",
                isError = true,
                selectedCountry = getRussiaCountry(),
                availableCountries = listOf(getRussiaCountry()).toImmutableList(),
                onValueChange = {},
                onCountyClick = {},
                onSelectCounty = {},
                onDismissCountyDropDownMenuClick = {},
            )
            TemplateInputPhone(
                value = "",
                label = "Телефон",
                isError = true,
                selectedCountry = getRussiaCountry(),
                availableCountries = listOf(getRussiaCountry()).toImmutableList(),
                errorText = "Smth wrong",
                onValueChange = {},
                onCountyClick = {},
                onSelectCounty = {},
                onDismissCountyDropDownMenuClick = {},
            )
        }
    }
}

private fun getRussiaCountry() =
    Country(
        name = "Russia",
        image = "",
        phoneCode = "+7",
        maskSymbol = '_',
        mask = "+7 (___) ___-__-__",
    )
