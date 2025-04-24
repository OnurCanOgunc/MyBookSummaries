package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.core.network.AndroidConnectivityObserver
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.data.repository.AuthRepositoryImpl
import com.decode.mybooksummaries.data.repository.BookRepositoryImpl
import com.decode.mybooksummaries.data.repository.MonthlyGoalRepositoryImpl
import com.decode.mybooksummaries.data.repository.QuoteRepositoryImpl
import com.decode.mybooksummaries.domain.repository.AuthRepository
import com.decode.mybooksummaries.domain.repository.BookRepository
import com.decode.mybooksummaries.domain.repository.MonthlyGoalRepository
import com.decode.mybooksummaries.domain.repository.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindsModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Binds
    @Singleton
    abstract fun bindMonthlyGoalRepository(impl: MonthlyGoalRepositoryImpl): MonthlyGoalRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(impl: AndroidConnectivityObserver): ConnectivityObserver
}