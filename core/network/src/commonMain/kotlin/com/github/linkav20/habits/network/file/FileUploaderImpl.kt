package com.github.linkav20.habits.network.file

import com.github.linkav20.habits.network.api.file.FileUploadApi
import com.github.linkav20.habits.network.api.file.FileUploader
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.buildPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.RawSource
import com.github.linkav20.habits.network.api.file.models.FileUploadResponseDto

internal class FileUploaderImpl(
    private val fileUploadApi: FileUploadApi,
) : FileUploader {

    override suspend fun uploadFile(
        name: String,
        file: ByteArray,
        onUpload: ((bytesSentTotal: Long, contentLength: Long?) -> Unit)?,
    ): FileUploadResponseDto {
        return withContext(Dispatchers.IO) {
            val multipart = MultiPartFormDataContent(
                formData {
                    append(
                        key = "uploadfile",
                        value = file,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=$name")
                        },
                    )
                },
            )

            fileUploadApi.uploadFile(multipart) {
                if (onUpload != null) {
                    onUpload(onUpload)
                } else {
                    onUpload(null)
                }
            }
        }
    }

    override suspend fun uploadFile(
        name: String,
        file: RawSource,
        size: Long,
        onUpload: ((bytesSentTotal: Long, contentLength: Long?) -> Unit)?,
    ): FileUploadResponseDto {
        return withContext(Dispatchers.IO) {
            val multipart = MultiPartFormDataContent(
                formData {
                    appendInput(
                        key = "uploadfile",
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=$name")
                        },
                    ) {
                        buildPacket {
                            write(source = file, byteCount = size)
                        }
                    }
                },
            )

            fileUploadApi.uploadFile(multipart) {
                if (onUpload != null) {
                    onUpload { sent, _ ->
                        onUpload(sent, size)
                    }
                } else {
                    onUpload(null)
                }
            }
        }
    }
}
