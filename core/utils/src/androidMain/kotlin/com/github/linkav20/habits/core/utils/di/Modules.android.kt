package com.github.linkav20.habits.core.utils.di

import com.github.linkav20.habits.core.utils.AndroidAppVisibility
import com.github.linkav20.habits.core.utils.AppVisibility

public actual fun appLifecycleListenerProvider(): AppVisibility = AndroidAppVisibility()
