package com.decode.mybooksummaries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val bookId: String? = "",
    val quote: String? = "",
    val isSynced: Boolean = false
)
