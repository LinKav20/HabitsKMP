package com.github.linkav20.habits.network.file

import com.github.linkav20.habits.network.NetworkConfig
import com.github.linkav20.habits.network.api.download.FileDownloader
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.utils.toPath
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

private const val DOCUMENTS = "documents"

class FileDownloaderImpl(
    private val networkConfig: NetworkConfig,
    private val platformDownloader: PlatformFileDownloader,
) : FileDownloader {
    override suspend fun downloadFile(
        fileUrl: String,
        onProgress: (Int) -> Unit,
        onDownloaded: (String) -> Unit,
        onError: () -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            val file = platformFileDocs(fileUrl)
            if (file.exists()) {
                onDownloaded(file.path)
                return@withContext
            }

            var success = false

            try {
                val downloadUrl = networkConfig.downloadUrl(fileUrl.encodeURLPath())
                platformDownloader.downloadFile(
                    url = downloadUrl,
                    destinationPath = file.path,
                    onProgress = onProgress,
                )
                success = true
                onDownloaded(file.path)
            } catch (e: CancellationException) {
                println("Download cancelled: ${e.message}")
            } catch (e: Exception) {
                onError.invoke()
                println("Error downloading file: ${e.message}")
            } finally {
                if (!success && file.exists()) file.delete()
            }
        }
    }

    override suspend fun getLocalFilePath(fileUrl: String): String? {
        if (fileUrl.isBlank()) return null
        val file = platformFileDocs(fileUrl)
        return if (file.exists()) file.path else null
    }

    private fun platformFileDocs(filePath: String): PlatformFile {
        val directory = PlatformFile(FileKit.cacheDir, "$DOCUMENTS/$filePath")
        if (!directory.exists()) {
            directory.createDirectories()
        }
        return directory / filePath.toPath().name
    }
}
