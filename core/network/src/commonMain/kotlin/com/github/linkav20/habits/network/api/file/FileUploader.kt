package com.github.linkav20.habits.network.api.file

import kotlinx.io.RawSource
import com.github.linkav20.habits.network.api.file.models.FileUploadResponseDto

interface FileUploader {

    suspend fun uploadFile(
        name: String,
        file: ByteArray,
        onUpload: ((bytesSentTotal: Long, contentLength: Long?) -> Unit)? = null,
    ): FileUploadResponseDto

    suspend fun uploadFile(
        name: String,
        file: RawSource,
        size: Long,
        onUpload: ((bytesSentTotal: Long, contentLength: Long?) -> Unit)? = null,
    ): FileUploadResponseDto
}
