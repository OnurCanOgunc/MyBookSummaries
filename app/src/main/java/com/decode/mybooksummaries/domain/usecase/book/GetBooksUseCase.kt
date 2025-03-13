package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(isConnected: Boolean): Flow<Response<List<Book>>> = bookRepository.getBooks(isConnected)
}