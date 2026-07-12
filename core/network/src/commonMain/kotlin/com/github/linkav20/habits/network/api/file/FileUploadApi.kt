package com.github.linkav20.habits.network.api.file

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.ReqBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import com.github.linkav20.habits.network.api.file.models.FileUploadResponseDto

interface FileUploadApi {

    @Multipart
    @POST("/load_file")
    suspend fun uploadFile(
        @Body file: MultiPartFormDataContent,
        @ReqBuilder ext: HttpRequestBuilder.() -> Unit,
    ): FileUploadResponseDto
}
