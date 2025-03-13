package com.decode.mybooksummaries.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["readingStatus"]),
        Index(value = ["finishedReadingDate"]),
        Index(value = ["title"]),
        Index(value = ["isDeleted"]),
        Index(value = ["isSynced"])
    ]
)
data class BookEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val author: String = "",
    val pageCount: String = "0",
    val currentPage: String = "0",
    val genre: String = "",
    val readingStatus: String = "To Read",
    val startedReadingDate: Long? = null,
    val finishedReadingDate: Long? = null,
    val summary: String = "",
    val imageUrl: String = "",
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)
