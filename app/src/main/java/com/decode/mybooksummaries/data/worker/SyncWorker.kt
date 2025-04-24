package com.decode.mybooksummaries.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toBook
import com.decode.mybooksummaries.data.mapper.toMonthlyGoal
import com.decode.mybooksummaries.data.mapper.toQuote
import com.decode.mybooksummaries.di.IoDispatcher
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val db: BookDatabase,
    private val bookRepository: BookRepository,
    private val monthlyGoalRepository: MonthlyGoalRepository,
    private val quoteRepository: QuoteRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "doWork() called - Synchronization begins")
        return try {
            withContext(ioDispatcher) {
                supervisorScope {

                    val unSyncedBooks = db.bookDao().getUnsyncedBooks()
                    unSyncedBooks.forEach { bookEntity ->
                        launch {
                            try {
                                val book = bookEntity.toBook()
                                val response = bookRepository.addBook(book, true)
                                if (response is Response.Success) {
                                    db.bookDao().updateBookSyncStatus(book.id)
                                } else {
                                    Log.e("SyncWorker", "Book sync failed for ID ${book.id}")
                                }
                            } catch (e: Exception) {
                                Log.e("SyncWorker", "Book sync error: ${e.message}")
                            }
                        }
                    }


                    val deletedBooks = db.bookDao().getDeletedUnsyncedBooks()
                    deletedBooks.forEach { bookEntity ->
                        launch {
                            try {
                                val deleteBook = bookEntity.toBook()
                                bookRepository.deleteBook(deleteBook.id, true)
                            } catch (e: Exception) {
                                Log.e("SyncWorker", "Book deletion sync error: ${e.message}")
                            }
                        }
                    }

                    val unSyncedGoals = db.monthlyGoalDao().getUnsyncedGoals()
                    unSyncedGoals.forEach { goalEntity ->
                        launch {
                            try {
                                val goal = goalEntity.toMonthlyGoal()
                                val response =
                                    monthlyGoalRepository.saveMonthlyGoal(goal.goalCount, true)
                                if (response is Response.Success) {
                                    db.monthlyGoalDao().updateMonthlyGoal(goal.id, true)
                                } else {
                                    Log.e(
                                        "SyncWorker",
                                        "Monthly target sync failed for ID ${goal.id}"
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    "SyncWorker",
                                    "Monthly target synchronization error: ${e.message}"
                                )
                            }
                        }
                    }

                    val unSyncedQuotes = db.quoteDao().getUnsyncedQuotes()
                    unSyncedQuotes.forEach { quoteEntity ->
                        launch {
                            try {
                                val quote = quoteEntity.toQuote()
                                val response = quoteRepository.addQuote(quote, true)
                                if (response is Response.Success) {
                                    db.quoteDao().updateQuoteSyncStatus(quote.id)
                                } else {
                                    Log.e("SyncWorker", "Quote sync failed for ID ${quote.id}")
                                }
                            } catch (e: Exception) {
                                Log.e("SyncWorker", "Quote sync error: ${e.message}")
                            }
                        }
                    }

                    val deleteQuote = db.quoteDao().getDeletedUnsyncedQuotes()
                    deleteQuote.forEach { quoteEntity ->
                        launch {
                            try {
                                val quote = quoteEntity.toQuote()
                                quoteRepository.deleteQuote(quote, true)
                            } catch (e: Exception) {
                                Log.e("SyncWorker", "Quote sync error: ${e.message}")
                            }
                        }
                    }
                }
            }

            Log.d("SyncWorker", "Synchronization completed")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "synchronization error: ${e.message}")
            Result.retry()
        }
    }
}