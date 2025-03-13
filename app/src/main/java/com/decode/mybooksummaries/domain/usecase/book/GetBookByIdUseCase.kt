package com.decode.mybooksummaries.domain.usecase.book

import com.decode.mybooksummaries.domain.repository.BookRepository
import javax.inject.Inject

class GetBookByIdUseCase @Inject constructor(private val repository: BookRepository) {
    suspend operator fun invoke(id: String, isConnected: Boolean) = repository.getBookById(id,isConnected)
}