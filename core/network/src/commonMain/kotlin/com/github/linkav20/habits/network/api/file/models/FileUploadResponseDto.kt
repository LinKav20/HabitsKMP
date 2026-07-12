package com.github.linkav20.habits.network.api.file.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileUploadResponseDto(
    @SerialName("metrics")
    val metrics: FileMetricsDto? = null,
    @SerialName("status")
    val status: FileStatusDto,
    @SerialName("error")
    val error: String? = null,
)

@Serializable
data class FileStatusDto(
    @SerialName("description") val description: String? = null,
    @SerialName("status") val status: Int,
)

@Serializable
data object FileMetricsDto
