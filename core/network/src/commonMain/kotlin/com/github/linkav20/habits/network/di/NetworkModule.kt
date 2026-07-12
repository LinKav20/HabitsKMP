@file:Suppress("MatchingDeclarationName")

package com.github.linkav20.habits.network.di

import com.github.linkav20.habits.network.GlobalAuthChannel
import com.github.linkav20.habits.network.NetworkConfig
import com.github.linkav20.habits.network.NetworkConfigImpl
import com.github.linkav20.habits.network.TemplateNetworkClient
import com.github.linkav20.habits.network.api.auth.AuthApi
import com.github.linkav20.habits.network.api.download.FileDownloader
import com.github.linkav20.habits.network.api.file.FileUploader
import com.github.linkav20.habits.network.file.FileDownloaderImpl
import com.github.linkav20.habits.network.file.FileUploaderImpl
import com.github.linkav20.habits.network.file.PlatformFileDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.dsl.onClose
import org.koin.mp.KoinPlatform.getKoin

object AuthorizedScope

val authorizedCoroutineScopeQualifier = StringQualifier("AuthScopeCoroutine")

fun KoinComponent.authorizedScope(): Scope {
    return getKoin().getOrCreateScope<AuthorizedScope>("Auth")
}

fun Module.authorizedScope(): Scope {
    return getKoin().getOrCreateScope<AuthorizedScope>("Auth")
}

expect val nativeNetworkModule: Module

val networkModule = module {
    includes(nativeNetworkModule)

    single { GlobalAuthChannel() }

    scope<AuthorizedScope> {
        scoped<CoroutineScope>(authorizedCoroutineScopeQualifier) {
            CoroutineScope(SupervisorJob() + Dispatchers.IO)
        } onClose { coroutineScope ->
            coroutineScope?.cancel()
        }
    }

    single<NetworkConfig> { NetworkConfigImpl(get()) }

    single<TemplateNetworkClient> {
        TemplateNetworkClient(
            networkConfig = get(),
            settingsStorage = get(),
            unauthUseCase = get(),
        )
    }

    single<FileUploader> {
        FileUploaderImpl(get())
    }

    single<FileDownloader> {
        val networkConfig = get<NetworkConfig>()
        FileDownloaderImpl(
            networkConfig = networkConfig,
            platformDownloader = get<PlatformFileDownloader>(),
        )
    }

    single<AuthApi> {
        get<TemplateNetworkClient>().auth
    }
}
