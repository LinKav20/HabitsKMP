package com.github.linkav20.habits.network.api.auth

import de.jensklingenberg.ktorfit.http.POST

interface AuthApi {
    @POST("/login")
    suspend fun login(): Boolean

    @POST("/logout")
    suspend fun logout(): Boolean
}
