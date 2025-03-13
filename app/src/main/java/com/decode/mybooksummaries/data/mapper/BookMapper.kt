package com.decode.mybooksummaries.data.mapper

import com.decode.mybooksummaries.data.local.entity.BookEntity
import com.decode.mybooksummaries.domain.model.Book
import com.google.firebase.Timestamp
import java.util.Date

fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
        pageCount = pageCount,
        currentPage = currentPage,
        genre = genre,
        readingStatus = readingStatus,
        startedReadingDate = startedReadingDate?.seconds,
        finishedReadingDate = finishedReadingDate?.seconds,
        summary = summary,
        imageUrl = imageUrl,
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        pageCount = pageCount,
        currentPage = currentPage,
        genre = genre,
        readingStatus = readingStatus,
        startedReadingDate = startedReadingDate?.let { Timestamp(Date(it * 1000)) },
        finishedReadingDate = finishedReadingDate?.let { Timestamp(Date(it * 1000)) },
        summary = summary,
        imageUrl = imageUrl,
    )
}