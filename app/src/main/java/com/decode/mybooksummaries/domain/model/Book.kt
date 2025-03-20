package com.decode.mybooksummaries.domain.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Book(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val author: String = "",
    val pageCount: String = "0",
    val currentPage: String = "0",
    val genre: String = "Select Genre",
    val readingStatus: String = "Queued",
    val startedReadingDate: Timestamp? = null,
    val finishedReadingDate: Timestamp? = null,
    val summary: String = "",
    val imageUrl: String = ""
)
