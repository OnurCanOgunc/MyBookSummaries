package com.decode.mybooksummaries.core.ui.extensions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp?.timestampToString(): String {
    return this?.let {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.format(it.toDate())
    } ?: ""
}