package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.data.repository.QuoteRepositoryImpl
import com.decode.mybooksummaries.data.local.dao.QuoteDao
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import com.decode.mybooksummaries.domain.usecase.QuoteUseCases
import com.decode.mybooksummaries.domain.usecase.quotes.AddQuoteUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.DeleteQuotesUseCase
import com.decode.mybooksummaries.domain.usecase.quotes.GetQuotesUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuoteModule {
    @Provides
    @Singleton
    fun provideQuoteRepository(
        @Named("quotesRef") quotesRef: CollectionReference,
        quotesDao: QuoteDao): QuoteRepository {
        return QuoteRepositoryImpl(quotesRef = quotesRef, quotesDao = quotesDao)
    }

    @Provides
    @Singleton
    fun provideQuoteUseCases(repository: QuoteRepository): QuoteUseCases {
        return QuoteUseCases(
            addQuote = AddQuoteUseCase(repository),
            getQuotes = GetQuotesUseCase(repository),
            deleteQuote = DeleteQuotesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    @Named("quotesRef")
    fun provideCollectionRef(auth: FirebaseAuth): CollectionReference {
        return Firebase.firestore.collection("users").document(auth.currentUser?.uid ?: "no user")
            .collection("quotes")
    }
}