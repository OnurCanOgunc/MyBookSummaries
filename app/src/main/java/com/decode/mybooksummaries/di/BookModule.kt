package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.data.repository.BookRepositoryImpl
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.decode.mybooksummaries.domain.usecase.BookUseCases
import com.decode.mybooksummaries.domain.usecase.book.AddBookUseCase
import com.decode.mybooksummaries.domain.usecase.book.DeleteBookUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBookByIdUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBooksByCategoryUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBooksReadThisMonthUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBooksUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetSearchBooksUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetTotalBooksReadUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookModule {
    @Provides
    @Singleton
    fun provideBookRepository(
        db: BookDatabase,
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): BookRepository = BookRepositoryImpl(
        firestore = firestore,
        auth = firebaseAuth,
        db = db)

    @Provides
    @Singleton
    fun provideBookUseCase(repository: BookRepository) = BookUseCases(
        addBook = AddBookUseCase(repository),
        getBooks = GetBooksUseCase(repository),
        getBookById = GetBookByIdUseCase(repository),
        getSearchBooks = GetSearchBooksUseCase(repository),
        deleteBook = DeleteBookUseCase(repository),
        getTotalBooksRead = GetTotalBooksReadUseCase(repository),
        getBooksReadThisMonth = GetBooksReadThisMonthUseCase(repository),
        getBooksByCategory = GetBooksByCategoryUseCase(repository)
    )

}