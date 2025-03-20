package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class GetBooksByCategoryUseCase @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke(category: String,isConnected: Boolean) = bookRepository.getBooksByCategory(category,isConnected)

}