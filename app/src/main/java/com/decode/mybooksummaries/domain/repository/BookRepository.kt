package com.decode.mybooksummaries.domain.repository

import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun addBook(book: Book, isConnected: Boolean): Response<Book>
    fun getBooks(isConnected: Boolean): Flow<Response<List<Book>>>
    suspend fun deleteBook(bookId: String, isConnected: Boolean): Response<String>
    suspend fun getBookById(id: String, isConnected: Boolean): Response<Book>
    fun getSearchBooks(query: String): Flow<Response<List<Book>>>
    suspend fun getTotalBooksRead(isConnected: Boolean): Response<Int>
    suspend fun getBooksReadThisMonth(): Response<Int>

}