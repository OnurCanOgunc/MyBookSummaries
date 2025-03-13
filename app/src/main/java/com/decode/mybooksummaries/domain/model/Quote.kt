package com.decode.mybooksummaries.domain.model

import java.util.UUID

data class Quote(
    val id: String = UUID.randomUUID().toString(),
    val bookId: String?="",
    val quote: String?="",
)
