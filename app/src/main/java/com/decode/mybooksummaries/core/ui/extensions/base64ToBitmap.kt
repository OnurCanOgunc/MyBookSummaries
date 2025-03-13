package com.decode.mybooksummaries.core.ui.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun String.base64ToBitmap(): Bitmap? {
    return runCatching {
        val decodedBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }.getOrNull()
}