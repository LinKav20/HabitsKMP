package com.github.linkav20.habits.network.file

import platform.Foundation.NSURLSession

interface FileDownloaderDelegate {

    fun downloadFile(
        urlSession: NSURLSession,
        url: String,
        destinationPath: String,
        onProgress: (progress: Int) -> Unit,
        onComplete: () -> Unit,
        onError: (error: String) -> Unit,
    ): Any

    fun cancelDownload(taskId: Any)
}
