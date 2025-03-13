package com.decode.mybooksummaries.data.repository

import android.util.Log
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.dao.BookDao
import com.decode.mybooksummaries.data.mapper.toBook
import com.decode.mybooksummaries.data.mapper.toBookEntity
import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named


class BookRepositoryImpl @Inject constructor(
    @Named("booksRef") private val booksRef: CollectionReference,
    private val bookDao: BookDao,
) : BookRepository {
    override suspend fun addBook(book: Book, isConnected: Boolean): Response<Book> {
        return runCatching {
            val bookEntity = book.toBookEntity().copy(isSynced = isConnected)
            bookDao.insertBook(bookEntity)
            if (isConnected) booksRef.document(book.id).set(book).await()
            Response.Success(book)
        }.getOrElse { e ->
            Response.Failure(e.message ?: "Unknown error occurred while adding book")
        }
    }

    override fun getBooks(isConnected: Boolean) = callbackFlow {
        if (isConnected) {
            Log.d("1BookRepositoryImpl", "İnternet bağlantısı mevcut Firstore,GetBooks")
            val listener = booksRef.orderBy("title").addSnapshotListener { snapshot, error ->
                error?.let {
                    trySend(Response.Failure(it.message ?: "Error getting books")).isSuccess
                    return@addSnapshotListener
                }

                val firestoreBooks =
                    snapshot?.documents?.mapNotNull { it.toObject(Book::class.java) }.orEmpty()
                val response = if (firestoreBooks.isEmpty()) Response.Empty else Response.Success(
                    firestoreBooks
                )

                trySend(response)
            }
            awaitClose { listener.remove() }
        } else {
            Log.d("1BookRepositoryImpl", "İnternet bağlantısı yok, SQLİYE")
            bookDao.getAllBooks()
                .map { books ->
                    if (books.isNotEmpty()) {
                        Response.Success(books.map { it.toBook() })
                    } else {
                        Response.Empty
                    }
                }
                .collect { response ->
                    trySend(response)
                }
            awaitClose()
        }
    }

    override suspend fun getBookById(id: String, isConnected: Boolean): Response<Book> {
        val cachedBook = bookDao.getBookById(id)?.toBook()
        if (cachedBook != null) return Response.Success(cachedBook)

        return if (isConnected) {
            runCatching {
                val snapshot = booksRef.document(id).get().await()
                snapshot.toObject(Book::class.java)?.let {
                    bookDao.insertBook(it.toBookEntity())
                    Response.Success(it)
                } ?: Response.Empty
            }.getOrElse { e ->
                Response.Failure(e.message ?: "Error fetching book")
            }
        } else {
            Response.Empty
        }
    }

    override fun getSearchBooks(query: String) = flow {
        bookDao.searchBooks(query).collect { bookEntities ->
            val books = bookEntities.map { it.toBook() }
            emit(if (books.isEmpty()) Response.Empty else Response.Success(books))
        }
    }.catch { e ->
        emit(Response.Failure(e.message ?: "Error searching books"))
    }

    override suspend fun deleteBook(bookId: String, isConnected: Boolean): Response<String> {
        Log.d("BookRepositoryImpl", "deleteBook: $bookId")

        return runCatching {
            bookDao.markBookAsDeleted(bookId)
            if (isConnected) {
                booksRef.document(bookId).delete().await()
                bookDao.deleteBookById(bookId)
            }
            Response.Success("Book deleted successfully")
        }.getOrElse { e ->
            Log.d("BookRepositoryImpl", "deleteBook: ${e.message}")
            Response.Failure(e.message ?: "Error deleting book")
        }
    }
}