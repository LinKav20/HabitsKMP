package com.github.linkav20.habits.datastore

import splitties.init.directBootCtx

actual fun dataStorePath(): String {
    return directBootCtx
        .filesDir
        .resolve(DATASTORE_FILENAME)
        .absolutePath
}
