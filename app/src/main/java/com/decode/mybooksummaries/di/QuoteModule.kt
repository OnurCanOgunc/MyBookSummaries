package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.data.repository.QuoteRepositoryImpl
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import com.decode.mybooksummaries.domain.usecase.QuoteUseCases
import com.decode.mybooksummaries.domain.usecase.quotes.AddQuoteUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteAllQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.GetQuotesUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideQuoteRepository(
       firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        db: BookDatabase): QuoteRepository {
        return QuoteRepositoryImpl(firestore = firestore, db = db,auth = firebaseAuth)
    }

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