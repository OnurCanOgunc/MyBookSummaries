package com.decode.mybooksummaries.domain.usecase.quotes

import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import javax.inject.Inject

class DeleteQuotesUseCase @Inject constructor(private val repository: QuoteRepository) {
    suspend operator fun invoke(quote: Quote, isConnected: Boolean) = repository.deleteQuote(quote, isConnected)
}