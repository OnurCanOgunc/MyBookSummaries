package com.decode.mybooksummaries.data.repository

import android.util.Log
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toBook
import com.decode.mybooksummaries.data.mapper.toBookEntity
import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Named


class BookRepositoryImpl @Inject constructor(
    @Named("booksRef") private val booksRef: CollectionReference,
    private val db: BookDatabase,
) : BookRepository {

    override suspend fun addBook(book: Book, isConnected: Boolean): Response<Book> {
        return runCatching {
            val bookEntity = book.toBookEntity().copy(isSynced = isConnected)
            db.bookDao().insertBook(bookEntity)
            if (isConnected) booksRef.document(book.id).set(book).await()
            Response.Success(book)
        }.getOrElse { e ->
            Log.d("BookRepositoryImpl", "addBook: ${e.message}")
            Response.Failure(e.message ?: "Unknown error occurred while adding book")
        }
    }

    override fun getBooks(isConnected: Boolean) = callbackFlow {
        if (isConnected) {
            val listener = booksRef.addSnapshotListener { snapshot, error ->
                error?.let {
                    trySend(Response.Failure(it.message ?: "Error getting books"))
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
            val localBooks = db.bookDao().getAllBooks().first()
                .map { books ->
                    books.toBook()
                }
            val response =
                if (localBooks.isEmpty()) Response.Empty else Response.Success(localBooks)
            trySend(response)
            awaitClose()
        }
    }

    override fun getBooksByCategory(
        category: String,
        isConnected: Boolean
    ): Flow<Response<List<Book>>> = callbackFlow {
        if (isConnected) {
            val listener = booksRef.whereEqualTo("genre", category)
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        trySend(Response.Failure(error.message ?: "firestore error"))
                        return@addSnapshotListener
                    }

                    val firestoreBooks =
                        snapshot?.documents?.mapNotNull { it.toObject(Book::class.java) }.orEmpty()
                    val response =
                        if (firestoreBooks.isEmpty()) Response.Empty else Response.Success(
                            firestoreBooks
                        )
                    trySend(response)
                }

            awaitClose { listener.remove() }
        } else {
            val localBooks = db.bookDao().getBooksByCategory(category)
                .first()
                .map { it.toBook() }
            val response =
                if (localBooks.isEmpty()) Response.Empty else Response.Success(localBooks)
            trySend(response)
        }
    }

    override suspend fun getBookById(id: String, isConnected: Boolean): Response<Book> {
        val cachedBook = db.bookDao().getBookById(id)?.toBook()
        if (cachedBook != null) return Response.Success(cachedBook)

        return if (isConnected) {
            runCatching {
                val snapshot = booksRef.document(id).get().await()
                snapshot.toObject(Book::class.java)?.let {
                    db.bookDao().insertBook(it.toBookEntity())
                    Response.Success(it)
                } ?: Response.Empty
            }.getOrElse { e ->
                Response.Failure(e.message ?: "Error fetching book")
            }
        } else {
            Response.Empty
        }
    }

    override fun getSearchBooks(query: String, isConnected: Boolean) = flow {
        db.bookDao().searchBooks(query).collect { bookEntities ->
            val books = bookEntities.map { it.toBook() }
            emit(if (books.isEmpty()) Response.Empty else Response.Success(books))
        }
    }.catch { e ->
        emit(Response.Failure(e.message ?: "Error searching books"))
    }

    override suspend fun getTotalBooksRead(isConnected: Boolean): Response<Int> {
        return try {
            val localCount = db.bookDao().getTotalBooksRead()

            if (localCount > 0) {
                return Response.Success(localCount)
            }

            if (isConnected) {
                val remoteCount =
                    booksRef.whereEqualTo("readingStatus", "Read").get().await().size()
                return Response.Success(remoteCount)
            }

            Response.Failure("No data available locally and no internet connection")
        } catch (e: Exception) {
            Response.Failure(e.message ?: "Error getting total books read")
        }
    }


    override suspend fun getBooksReadThisMonth(): Response<Int> {
        return try {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val firstDayOfMonthMillis = calendar.timeInMillis

            val localCount = db.bookDao().getBooksReadThisMonth(firstDayOfMonthMillis)
            if (localCount > 0) return Response.Success(localCount)

            val firstDayOfMonthTimestamp = Timestamp(calendar.time)
            val snapshot = booksRef
                .whereEqualTo("readingStatus", "Read")
                .whereGreaterThanOrEqualTo("finishedReadingDate", firstDayOfMonthTimestamp)
                .get()
                .await()
            snapshot.toObjects(Book::class.java).forEach { book ->
                db.bookDao().insertBook(book.toBookEntity())
            }
            Log.d("BookRepositoryImpl", "getBooksReadThisMonth: ${snapshot.size()}")
            Response.Success(snapshot.size())
        } catch (e: Exception) {
            Log.d("BookRepositoryImpl", "<getBooksReadThisMonth>: ${e.message}")
            Response.Failure(e.message ?: "Error getting books read this month")
        }
    }

    override suspend fun deleteBook(bookId: String, isConnected: Boolean): Response<String> {
        Log.d("BookRepositoryImpl", "deleteBook: $bookId")

        return runCatching {
            if (isConnected) {
                db.bookDao().deleteBookById(bookId)
                booksRef.document(bookId).delete().await()
            } else db.bookDao().markBookAsDeleted(bookId)
            Log.d("BookRepositoryImpl", "deleteBook: Book deleted successfully")
            Response.Success("Book deleted successfully")
        }.getOrElse { e ->
            Log.d("BookRepositoryImpl", "deleteBook: ${e.message}")
            Response.Failure(e.message ?: "Error deleting book")
        }
    }
}