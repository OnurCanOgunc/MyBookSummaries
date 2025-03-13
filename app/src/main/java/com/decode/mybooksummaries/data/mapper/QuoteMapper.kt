package com.decode.mybooksummaries.data.mapper

import com.decode.mybooksummaries.data.local.entity.QuoteEntity
import com.decode.mybooksummaries.domain.model.Quote

fun QuoteEntity.toQuote(): Quote {
    return Quote(
        id = id,
        bookId = bookId,
        quote = quote,
    )
}

fun Quote.toQuoteEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        bookId = bookId,
        quote = quote,
    )
}