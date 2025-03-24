package com.decode.mybooksummaries.data.repository

import android.util.Log
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toBook
import com.decode.mybooksummaries.data.mapper.toBookEntity
import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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

class BookRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val db: BookDatabase,
) : BookRepository {

    private val booksRef: CollectionReference
        get() = firestore.collection("users").document(auth.currentUser?.uid ?: "")
            .collection("books")

    override suspend fun addBook(book: Book, isConnected: Boolean): Response<Book> {
        return runCatching {
            val bookEntity = book.toBookEntity().copy(isSynced = isConnected)
            db.bookDao().insertBook(bookEntity)
            if (isConnected) booksRef.document(book.id)
                .set(book.copy(userId = auth.currentUser?.uid)).await()
            Response.Success(book)
        }.getOrElse { e ->
            Log.d("BookRepositoryImpl", "addBook: ${e.message}")
            Response.Failure(e.message ?: "Unknown error occurred while adding book")
        }
    }

    override fun getBooks(isConnected: Boolean) = callbackFlow {
        if (isConnected) {
            val listener = booksRef
                .addSnapshotListener { snapshot, error ->
                    error?.let {
                        trySend(Response.Failure(it.message ?: "Error getting books"))
                        return@addSnapshotListener
                    }

                    val firestoreBooks = snapshot?.documents
                        ?.mapNotNull { it.toObject(Book::class.java) }
                        .orEmpty()

                    Log.d("BookRepositoryImpl", "Fetched books: $firestoreBooks")
                    trySend(
                        if (firestoreBooks.isEmpty()) Response.Empty else Response.Success(
                            firestoreBooks
                        )
                    )
                }
            awaitClose { listener.remove() }
        } else {
            val localBooks = db.bookDao().getAllBooks().first().map { it.toBook() }
            trySend(if (localBooks.isEmpty()) Response.Empty else Response.Success(localBooks))
            awaitClose()
        }
    }

    override fun getBooksByCategory(
        category: String,
        isConnected: Boolean
    ): Flow<Response<List<Book>>> = callbackFlow {
        if (isConnected) {
            val listener = booksRef
                .whereEqualTo("genre", category)
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
            awaitClose()
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
            if (isConnected) {
                val remoteCount =
                    booksRef
                        .whereEqualTo("readingStatus", "Finished").get().await().size()
                Log.d("BookRepositoryImpl", "getTotalBooksRead: $remoteCount")
                return Response.Success(remoteCount)
            } else {
                val localCount = db.bookDao().getTotalBooksRead()
                Response.Success(localCount)
            }
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
                .whereEqualTo("readingStatus", "Finished")
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