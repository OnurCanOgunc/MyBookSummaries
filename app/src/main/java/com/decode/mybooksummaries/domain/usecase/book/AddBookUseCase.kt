package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class AddBookUseCase @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(book: Book, isConnected: Boolean): Response<Book> {
        return bookRepository.addBook(book,isConnected)
    }
}