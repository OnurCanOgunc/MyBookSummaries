package com.decode.mybooksummaries.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.decode.mybooksummaries.data.local.dao.BookDao
import com.decode.mybooksummaries.data.mapper.toBook
import com.decode.mybooksummaries.domain.repository.BookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val bookDao: BookDao,
    private val bookRepository: BookRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "doWork() called - Senkronizasyon başlıyor")
        return try {
            supervisorScope {

                val unSyncedBooks = bookDao.getUnsyncedBooks()
                unSyncedBooks.forEach { bookEntity ->
                    launch {
                        try{
                            val book = bookEntity.toBook()
                            bookRepository.addBook(book, true)
                            bookDao.updateBookSyncStatus(book.id)
                        } catch (e: Exception){
                            Log.e("SyncWorker", "Kitap senkronizasyon hatası: ${e.message}")
                        }
                    }
                }


                val deletedBooks = bookDao.getDeletedUnsyncedBooks()
                deletedBooks.forEach { bookEntity ->
                    launch {
                        try{
                            val deleteBook = bookEntity.toBook()
                            bookRepository.deleteBook(deleteBook.id, true)
                        } catch (e: Exception){
                            Log.e("SyncWorker", "Kitap silme senkronizasyon hatası: ${e.message}")
                        }
                    }
                }
            }

            Log.d("SyncWorker", "Senkronizasyon tamamlandı")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Genel senkronizasyon hatası: ${e.message}")
            Result.retry()
        }
    }
}