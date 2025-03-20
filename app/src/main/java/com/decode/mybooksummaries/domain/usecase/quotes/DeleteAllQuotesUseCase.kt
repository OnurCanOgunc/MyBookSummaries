package com.decode.mybooksummaries.domain.usecase.quotes

import com.decode.mybooksummaries.domain.repository.QuoteRepository
import javax.inject.Inject

class DeleteAllQuotesUseCase @Inject constructor(private val repository: QuoteRepository) {
    suspend operator fun invoke(isConnected: Boolean) = repository.deleteAllQuotes(isConnected)
}