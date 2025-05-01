package com.decode.mybooksummaries.di

import android.content.Context
import androidx.work.WorkManager
import com.decode.mybooksummaries.data.worker.WorkScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideSyncWorkManager(workManager: WorkManager): WorkScheduler {
        return WorkScheduler(workManager)
    }
}