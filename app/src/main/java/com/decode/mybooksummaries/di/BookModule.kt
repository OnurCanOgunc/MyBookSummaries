package com.decode.mybooksummaries.di

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