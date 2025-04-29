package com.decode.mybooksummaries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.decode.mybooksummaries.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("DELETE FROM books WHERE isSynced = 1 AND id NOT IN (SELECT id FROM books)")
    suspend fun clearSyncedBooks()

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' COLLATE NOCASE")
    fun searchBooks(query: String):  Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE userId = :userId AND readingStatus = 'Reading' AND lastUpdated < :thresholdTimeMillis AND isDeleted = 0")
    suspend fun getReadingBooksInactiveSince(userId: String, thresholdTimeMillis: Long): List<BookEntity>

    @Query("SELECT * FROM books WHERE readingStatus = :status")
    suspend fun getBooksByStatus(status: String): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: String)

    @Query("SELECT * FROM books WHERE isSynced = 0")
    suspend fun getUnsyncedBooks(): List<BookEntity>

    @Query("SELECT * FROM books WHERE isDeleted = 1")
    suspend fun getDeletedUnsyncedBooks(): List<BookEntity>

    @Query("UPDATE books SET isSynced = 1 WHERE id = :bookId")
    suspend fun updateBookSyncStatus(bookId: String)

    @Query("UPDATE books SET isDeleted = 1 WHERE id = :bookId")
    suspend fun markBookAsDeleted(bookId: String)

    @Query("SELECT COUNT(*) FROM books WHERE readingStatus = 'Read'")
    suspend fun getTotalBooksRead(): Int

    @Query("SELECT COUNT(*) FROM books WHERE readingStatus = 'Read' AND finishedReadingDate >= :startOfMonth")
    suspend fun getBooksReadThisMonth(startOfMonth: Long): Int

    @Query("SELECT * FROM books WHERE genre = :category")
    fun getBooksByCategory(category: String): Flow<List<BookEntity>>

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
}