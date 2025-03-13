package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class GetBooksReadThisMonthUseCase @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke() = bookRepository.getBooksReadThisMonth()

}