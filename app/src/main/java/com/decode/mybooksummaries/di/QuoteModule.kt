package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.domain.repository.QuoteRepository
import com.decode.mybooksummaries.domain.usecase.QuoteUseCases
import com.decode.mybooksummaries.domain.usecase.quotes.AddQuoteUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteAllQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.GetQuotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuoteModule {

    @Provides
    @Singleton
    fun provideQuoteUseCases(repository: QuoteRepository): QuoteUseCases {
        return QuoteUseCases(
            addQuote = AddQuoteUseCase(repository),
            getQuotes = GetQuotesUseCase(repository),
            deleteQuote = DeleteQuotesUseCase(repository),
            deleteAllQuotes = DeleteAllQuotesUseCase(repository)
        )
    }
}