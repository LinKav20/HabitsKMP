package com.github.linkav20.habits.core.utils

public interface AppVisibility {

    public interface Listener {
        public fun onAppGoesForeground()
        public fun onAppGoesBackground()
    }

    public fun visible(): Boolean

    public fun registerListener(listener: Listener)
    public fun unregisterListener(listener: Listener)
}
