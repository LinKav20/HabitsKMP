package com.github.linkav20.habits.core.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

public class AndroidAppVisibility : AppVisibility {
    private val listeners = mutableListOf<AppVisibility.Listener>()
    private var isVisible = false

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            isVisible = true
            notifyListeners { it.onAppGoesForeground() }
        }

        override fun onStop(owner: LifecycleOwner) {
            isVisible = false
            notifyListeners { it.onAppGoesBackground() }
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    override fun visible(): Boolean = isVisible

    override fun registerListener(listener: AppVisibility.Listener) {
        synchronized(listeners) {
            listeners.add(listener)
        }
    }

    override fun unregisterListener(listener: AppVisibility.Listener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    private fun notifyListeners(action: (AppVisibility.Listener) -> Unit) {
        synchronized(listeners) {
            listeners.toList()
        }.forEach(action)
    }
}
