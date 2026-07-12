package com.github.linkav20.habits.core.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

public actual fun ImageBitmap.asByteArray(extension: String): ByteArray {
    val format = if (extension == "png") EncodedImageFormat.PNG else EncodedImageFormat.JPEG
    val image = Image.makeFromBitmap(this.asSkiaBitmap())
    val imageData = image.encodeToData(format)
    return imageData?.bytes ?: ByteArray(0)
}
