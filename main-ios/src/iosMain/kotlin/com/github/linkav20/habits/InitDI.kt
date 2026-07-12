package com.github.linkav20.habits

import org.koin.dsl.module
import com.github.linkav20.habits.compose.di.initKoin
import com.github.linkav20.habits.network.file.FileDownloaderDelegate

@Suppress("unused", "FunctionName")
fun InitDI(
    fileDownloaderDelegate: FileDownloaderDelegate,
) = initKoin(
    listOf(
        module {
            single<FileDownloaderDelegate> { fileDownloaderDelegate }
        },
    ),
)
