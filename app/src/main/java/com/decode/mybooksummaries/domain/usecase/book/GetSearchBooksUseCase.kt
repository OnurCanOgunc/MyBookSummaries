package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class GetSearchBooksUseCase @Inject constructor(private val bookRepository: BookRepository)  {
    operator fun invoke(query: String) = bookRepository.getSearchBooks(query)

}