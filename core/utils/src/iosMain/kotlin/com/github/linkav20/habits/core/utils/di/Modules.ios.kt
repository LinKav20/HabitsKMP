package com.github.linkav20.habits.core.utils.di

import com.github.linkav20.habits.core.utils.AppVisibility
import com.github.linkav20.habits.core.utils.IosAppVisibility
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillEnterForegroundNotification
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSObjectProtocol

public actual fun appLifecycleListenerProvider(): AppVisibility = IosAppVisibility()
