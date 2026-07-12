package com.github.linkav20.habits.network.file

import com.github.linkav20.habits.network.file.PlatformFileDownloader
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttpEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidPlatformFileDownloader(private val httpClient: HttpClient) : PlatformFileDownloader {

    private val okHttpClient: OkHttpClient by lazy {
        val ktorEngine = httpClient.engine as? OkHttpEngine
            ?: throw error("Expected OkHttp engine, got ${httpClient.engine::class.simpleName}")

        ktorEngine.config.preconfigured ?: OkHttpClient.Builder().build()
    }

    override suspend fun downloadFile(
        url: String,
        destinationPath: String,
        onProgress: (progress: Int) -> Unit,
    ) = withContext(Dispatchers.IO) {
        val destinationFile = File(destinationPath)

        destinationFile.parentFile?.mkdirs()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = suspendCancellableCoroutine<Response> { continuation ->
            val call = okHttpClient.newCall(request)

            continuation.invokeOnCancellation {
                call.cancel()
            }

            call.enqueue(object : Callback {
                override fun onFailure(
                    call: Call,
                    e: IOException,
                ) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(
                    call: Call,
                    response: Response,
                ) {
                    continuation.resume(response)
                }
            })
        }

        try {
            if (!response.isSuccessful) {
                throw IOException("HTTP ${response.code}: ${response.message}")
            }

            val body = response.body ?: throw IOException("Empty response body")
            val contentLength = body.contentLength()
            var totalBytesRead = 0L
            var lastProgress = -1

            destinationFile.sink().buffer().use { sink ->
                body.source().use { source ->
                    val bufferSize = 8192L
                    var bytesRead: Long

                    while (source.read(sink.buffer, bufferSize).also { bytesRead = it } != -1L) {
                        ensureActive()

                        totalBytesRead += bytesRead
                        sink.flush()

                        if (contentLength > 0) {
                            val progress = ((totalBytesRead * 100) / contentLength).toInt().coerceIn(0, 100)
                            if (progress != lastProgress) {
                                onProgress(progress)
                                lastProgress = progress
                            }
                        }
                    }
                }
            }
        } catch (e: CancellationException) {
            if (destinationFile.exists()) {
                destinationFile.delete()
            }
            throw e
        } catch (e: Exception) {
            if (destinationFile.exists()) {
                destinationFile.delete()
            }
            throw e
        } finally {
            response.close()
        }
    }
}
