package com.decode.mybooksummaries.domain.usecase.quotes

import com.decode.mybooksummaries.domain.repository.QuoteRepository
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(private val repository: QuoteRepository) {
    operator fun invoke(bookId: String, isConnected: Boolean) = repository.getQuotes(bookId, isConnected)

}