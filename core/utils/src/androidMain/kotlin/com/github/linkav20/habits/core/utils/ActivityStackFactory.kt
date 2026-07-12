package com.github.linkav20.habits.core.utils

import android.app.Activity
import java.lang.ref.WeakReference

public object ActivityStackFactory {
    private val activities = ArrayDeque<WeakReference<Activity>>()

    public fun push(activity: Activity) {
        activities.addLast(WeakReference(activity))
        cleanup()
    }

    public fun pop(activity: Activity) {
        activities.removeAll { it.get() == activity || it.get() == null }
        cleanup()
    }

    private fun cleanup() {
        activities.removeAll { it.get() == null }
    }

    public fun getContext(): Activity? {
        cleanup()
        return activities.lastOrNull()?.get()
    }
}
