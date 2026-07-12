package com.github.linkav20.habits.network

interface NetworkConfig {
    val isStage: Boolean
    val baseUrl: String
    fun downloadUrl(fileName: String): String
}
