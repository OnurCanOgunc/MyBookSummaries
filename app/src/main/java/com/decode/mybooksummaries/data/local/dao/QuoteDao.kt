package com.decode.mybooksummaries.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.decode.mybooksummaries.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quote: List<QuoteEntity>)

    @Query("DELETE FROM quotes WHERE id = :quoteId")
    suspend fun deleteQuote(quoteId: String)

    @Query("UPDATE quotes SET isDeleted = 1 WHERE id = :quoteId")
    suspend fun markQuoteAsDeleted(quoteId: String)

    @Query("SELECT * FROM quotes WHERE isDeleted = 1")
    suspend fun getDeletedUnsyncedQuotes(): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE bookId = :bookId")
    fun getQuotes(bookId: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE isSynced = 0")
    suspend fun getUnsyncedQuotes(): List<QuoteEntity>

    @Query("UPDATE quotes SET isSynced = 1 WHERE id = :quoteId")
    suspend fun updateQuoteSyncStatus(quoteId: String)

    @Query("DELETE FROM QUOTES")
    suspend fun deleteAllQuotes()
}