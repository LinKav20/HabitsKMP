package com.github.linkav20.habits.core.utils

import platform.Foundation.NSUUID

public actual fun randomUUID(): String = NSUUID().UUIDString()
