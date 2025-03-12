package com.decode.mybooksummaries.di

import android.content.Context
import com.decode.mybooksummaries.core.network.AndroidConnectivityObserver
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {
    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return AndroidConnectivityObserver(context)
    }
}