package com.decode.mybooksummaries.data.worker

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun startOneTimeSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueueUniqueWork(
            "SyncWorker",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    fun startPeriodicSync() {
        val periodicRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            2, TimeUnit.DAYS
        ).setConstraints(
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()
        ).setInitialDelay(2, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "SyncWorker_Periodic",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }
}