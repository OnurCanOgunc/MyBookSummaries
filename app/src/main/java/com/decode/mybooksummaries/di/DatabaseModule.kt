package com.decode.mybooksummaries.di

import android.content.Context
import androidx.room.Room
import com.decode.mybooksummaries.data.local.dao.BookDao
import com.decode.mybooksummaries.data.local.dao.QuoteDao
import com.decode.mybooksummaries.data.local.db.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBookDao(bookDatabase: BookDatabase): BookDao {
        return bookDatabase.bookDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): BookDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            BookDatabase::class.java,
            "books_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(bookDatabase: BookDatabase): QuoteDao {
        return bookDatabase.quoteDao()
    }
}