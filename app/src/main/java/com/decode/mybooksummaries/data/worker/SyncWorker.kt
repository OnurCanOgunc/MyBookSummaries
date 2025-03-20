package com.decode.mybooksummaries.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.mapper.toBook
import com.decode.mybooksummaries.data.mapper.toMonthlyGoal
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val db: BookDatabase,
    private val bookRepository: BookRepository,
    private val monthlyGoalRepository: MonthlyGoalRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "doWork() called - Synchronization begins")
        return try {
            supervisorScope {

                val unSyncedBooks = db.bookDao().getUnsyncedBooks()
                unSyncedBooks.forEach { bookEntity ->
                    launch {
                        try{
                            val book = bookEntity.toBook()
                            bookRepository.addBook(book, true)
                            db.bookDao().updateBookSyncStatus(book.id)
                        } catch (e: Exception){
                            Log.e("SyncWorker", "Book sync error: ${e.message}")
                        }
                    }
                }


                val deletedBooks = db.bookDao().getDeletedUnsyncedBooks()
                deletedBooks.forEach { bookEntity ->
                    launch {
                        try{
                            val deleteBook = bookEntity.toBook()
                            bookRepository.deleteBook(deleteBook.id, true)
                        } catch (e: Exception){
                            Log.e("SyncWorker", "Book deletion sync error: ${e.message}")
                        }
                    }
                }

                val unSyncedGoals = db.monthlyGoalDao().getUnsyncedGoals()
                unSyncedGoals.forEach { goalEntity ->
                    launch {
                        try{
                            val goal = goalEntity.toMonthlyGoal()
                            monthlyGoalRepository.saveMonthlyGoal(goal.goalCount, true)
                            db.monthlyGoalDao().updateMonthlyGoal(goal.id, true)
                        } catch (e: Exception){
                            Log.e("SyncWorker", "Monthly target synchronization error: ${e.message}")
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