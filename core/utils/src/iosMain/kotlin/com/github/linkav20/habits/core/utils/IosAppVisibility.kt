package com.github.linkav20.habits.core.utils

import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.darwin.NSObjectProtocol

public class IosAppVisibility : AppVisibility {
    private val listeners = mutableListOf<AppVisibility.Listener>()
    private var isVisible = false

    private val foregroundObserver: NSObjectProtocol
    private val backgroundObserver: NSObjectProtocol

    init {
        foregroundObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = null,
        ) {
            isVisible = true
            notifyListeners { it.onAppGoesForeground() }
        }

        backgroundObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = null,
        ) {
            isVisible = false
            notifyListeners { it.onAppGoesBackground() }
        }
    }

    override fun visible(): Boolean = isVisible

    override fun registerListener(listener: AppVisibility.Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: AppVisibility.Listener) {
        listeners.remove(listener)
    }

    private fun notifyListeners(action: (AppVisibility.Listener) -> Unit) {
        listeners.toList().forEach(action)
    }

    public fun cleanup() {
        NSNotificationCenter.defaultCenter.removeObserver(foregroundObserver)
        NSNotificationCenter.defaultCenter.removeObserver(backgroundObserver)
    }
}
