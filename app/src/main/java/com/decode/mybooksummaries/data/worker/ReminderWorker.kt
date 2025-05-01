package com.decode.mybooksummaries.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.decode.mybooksummaries.core.notifications.NotificationUtils
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val db: BookDatabase,
    private val monthlyGoalRepository: MonthlyGoalRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
                checkReadingReminder()
                checkMonthlyGoalReminder()
                Result.success()
            } catch (e: Exception) {
                Log.e("ReminderWorker", "Reminder error: ${e.message}")
                Result.retry()
            }
    }


    private suspend fun checkReadingReminder() {
        val readingBooks = db.bookDao().getBooksByStatus("Reading")
        val now = System.currentTimeMillis()
        val threshold = 3 * 24 * 60 * 60 * 1000 // 3 gün

        val notReadFor3Days = readingBooks.filter {
            it.lastUpdated?.let { lastReadAt ->
                now - lastReadAt > threshold
            } == true
        }

        if (notReadFor3Days.isNotEmpty()) {
            NotificationUtils.sendNotification(
                context = applicationContext,
                title = "Okumaya Devam Et!",
                message = "En son bıraktığın kitabı okumaya devam etmek ister misin?",
            )
        }
    }

    private suspend fun checkMonthlyGoalReminder() {
        val response = monthlyGoalRepository.getMonthlyGoal(true).first()

        if (response is Response.Empty) {
            NotificationUtils.sendNotification(
                context = applicationContext,
                title = "Yeni Ay, Yeni Hedef!",
                message = "Henüz bu ay için okuma hedefi belirlemedin. Hedef koymak ister misin?",
                navigateToEditProfile = true
            )
        }
    }
}