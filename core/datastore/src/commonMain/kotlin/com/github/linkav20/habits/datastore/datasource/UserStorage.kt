package com.github.linkav20.habits.datastore.datasource

import kotlinx.serialization.Serializable

@Serializable
data class UserStorage(
    val id: String = "",
    val name: String = "",
)
