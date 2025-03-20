package com.decode.mybooksummaries.data.mapper

import com.decode.mybooksummaries.data.local.entity.QuoteEntity
import com.decode.mybooksummaries.domain.model.Quote

fun Quote.toQuoteEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        bookId = bookId,
        quote = quote,
    )
}