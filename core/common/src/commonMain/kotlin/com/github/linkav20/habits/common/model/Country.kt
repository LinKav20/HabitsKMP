package com.github.linkav20.habits.common.model

import kotlinx.serialization.Serializable

@Serializable
public data class Country(
    val name: String,
    val image: String,
    val phoneCode: String,
    val mask: String,
    val maskSymbol: Char,
)
