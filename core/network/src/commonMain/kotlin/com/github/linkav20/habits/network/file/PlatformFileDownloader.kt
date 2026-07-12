package com.github.linkav20.habits.network.file

/**
 * Common interface for platform-specific file downloaders.
 * Provides memory-efficient streaming downloads with progress tracking.
 */
interface PlatformFileDownloader {

    /**
     * Downloads a file using platform-optimized streaming to avoid memory issues.
     *
     * @param url Complete URL to download from
     * @param destinationPath Local file path where to save the file
     * @param onProgress Progress callback with percentage (0-100)
     * @throws Exception if download fails
     */
    suspend fun downloadFile(
        url: String,
        destinationPath: String,
        onProgress: (progress: Int) -> Unit = {},
    )
}
