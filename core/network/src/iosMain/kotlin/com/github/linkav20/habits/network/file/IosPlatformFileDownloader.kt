package com.github.linkav20.habits.network.file

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.DarwinClientEngineConfig
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IosPlatformFileDownloader(
    private val httpClient: HttpClient,
    private val fileDownloaderDelegate: FileDownloaderDelegate,
) : PlatformFileDownloader {

    private val urlSession: NSURLSession
        get() {
            return (httpClient.engine.config as? DarwinClientEngineConfig)?.preconfiguredSession
                ?: NSURLSession.sharedSession
        }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun downloadFile(
        url: String,
        destinationPath: String,
        onProgress: (progress: Int) -> Unit,
    ) {
        val destinationUrl = NSURL.fileURLWithPath(destinationPath)

        val parentDir = destinationUrl.URLByDeletingLastPathComponent
        if (parentDir != null) {
            NSFileManager.defaultManager.createDirectoryAtURL(
                url = parentDir,
                withIntermediateDirectories = true,
                attributes = null,
                error = null,
            )
        }

        suspendCancellableCoroutine { continuation ->
            val taskId = fileDownloaderDelegate.downloadFile(
                urlSession = urlSession,
                url = url,
                destinationPath = destinationPath,
                onProgress = onProgress,
                onComplete = {
                    continuation.resume(Unit)
                },
                onError = { error ->
                    continuation.resumeWithException(
                        IOException("Download failed: $error"),
                    )
                },
            )

            continuation.invokeOnCancellation {
                fileDownloaderDelegate.cancelDownload(taskId)

                if (NSFileManager.defaultManager.fileExistsAtPath(destinationPath)) {
                    NSFileManager.defaultManager.removeItemAtURL(destinationUrl, error = null)
                }
            }
        }
    }
}

private class IOException(message: String) : Exception(message)
