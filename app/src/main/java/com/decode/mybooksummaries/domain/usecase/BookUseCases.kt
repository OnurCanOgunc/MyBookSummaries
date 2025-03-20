package com.decode.mybooksummaries.domain.usecase

import com.decode.mybooksummaries.domain.usecase.book.AddBookUseCase
import com.decode.mybooksummaries.domain.usecase.book.DeleteBookUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBookByIdUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBooksByCategoryUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBooksUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetSearchBooksUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetBooksReadThisMonthUseCase
import com.decode.mybooksummaries.domain.usecase.book.GetTotalBooksReadUseCase

data class BookUseCases(
    val addBook: AddBookUseCase,
    val getBooks: GetBooksUseCase,
    val getBookById: GetBookByIdUseCase,
    val getSearchBooks: GetSearchBooksUseCase,
    val deleteBook: DeleteBookUseCase,
    val getTotalBooksRead: GetTotalBooksReadUseCase,
    val getBooksReadThisMonth: GetBooksReadThisMonthUseCase,
    val getBooksByCategory: GetBooksByCategoryUseCase
)
