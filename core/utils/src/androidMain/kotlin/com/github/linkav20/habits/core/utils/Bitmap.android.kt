package com.github.linkav20.habits.core.utils

import android.graphics.Bitmap.CompressFormat
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

public actual fun ImageBitmap.asByteArray(extension: String): ByteArray {
    val format = if (extension == "png") CompressFormat.PNG else CompressFormat.JPEG
    val stream = ByteArrayOutputStream()
    this.asAndroidBitmap().compress(format, 100, stream)
    return stream.toByteArray()
}
