package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class GetTotalBooksReadUseCase @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke(connected: Boolean): Response<Int> {
        return bookRepository.getTotalBooksRead(connected)
    }
}