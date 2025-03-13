package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(bookId: String, isConnected: Boolean): Response<String> {
        return bookRepository.deleteBook(bookId,isConnected)
    }
}
