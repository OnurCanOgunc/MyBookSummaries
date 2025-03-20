package com.decode.mybooksummaries.domain.usecase

import com.decode.mybooksummaries.domain.usecase.quotes.AddQuoteUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteAllQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.GetQuotesUseCase

data class QuoteUseCases(
    val addQuote: AddQuoteUseCase,
    val getQuotes: GetQuotesUseCase,
    val deleteQuote: DeleteQuotesUseCase,
    val deleteAllQuotes: DeleteAllQuotesUseCase
)
