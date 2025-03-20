package com.decode.mybooksummaries.presentation.detail.util

fun splitTextByWords(summary: String, chunkSize: Int): List<String> {
    val words = summary.trim().split("\\s+".toRegex())
    val pages = mutableListOf<String>()
    var currentPage = ""

    words.forEach { word ->
        val newPage = if (currentPage.isEmpty()) word else "$currentPage $word"
        if (newPage.length <= chunkSize) {
            currentPage = newPage
        } else {
            pages.add(currentPage)
            currentPage = word
        }
    }

    if (currentPage.isNotEmpty()) {
        pages.add(currentPage)
    }

    return pages
}