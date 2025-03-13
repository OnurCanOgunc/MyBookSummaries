package com.decode.mybooksummaries.presentation.addbook.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return runCatching {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    }.getOrNull()
}