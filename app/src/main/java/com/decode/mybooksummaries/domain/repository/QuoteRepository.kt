package com.decode.mybooksummaries.domain.repository

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    suspend fun addQuote(quote: Quote, isConnected: Boolean): Response<Quote>
    fun getQuotes(bookId: String, isConnected: Boolean): Flow<Response<List<Quote>>>
    suspend fun deleteQuote(quote: Quote, isConnected: Boolean): Response<String>
    suspend fun deleteAllQuotes(isConnected: Boolean): Response<String>

}