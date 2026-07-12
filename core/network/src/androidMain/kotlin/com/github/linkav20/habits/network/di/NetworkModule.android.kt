package com.github.linkav20.habits.network.di

import org.koin.core.module.Module
import org.koin.dsl.module
import com.github.linkav20.habits.network.TemplateNetworkClient
import com.github.linkav20.habits.network.file.AndroidPlatformFileDownloader
import com.github.linkav20.habits.network.file.PlatformFileDownloader

actual val nativeNetworkModule: Module = module {
    single<PlatformFileDownloader> {
        AndroidPlatformFileDownloader(
            httpClient = get<TemplateNetworkClient>().authorizedHttpClient,
        )
    }
}
