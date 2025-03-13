package com.decode.mybooksummaries.data.workers

import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import javax.inject.Inject

class SyncWorkManager @Inject constructor(
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
        Log.d("SyncWorkManager", "Tek seferlik senkronizasyon başlatıldı")
    }
}