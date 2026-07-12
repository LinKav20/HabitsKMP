package com.github.linkav20.habits.network.api.download

interface FileDownloader {
    suspend fun downloadFile(
        fileUrl: String,
        onProgress: (percent: Int) -> Unit,
        onDownloaded: (filePath: String) -> Unit,
        onError: () -> Unit,
    )

    suspend fun getLocalFilePath(fileUrl: String): String?
}
