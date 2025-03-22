package com.decode.mybooksummaries.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.decode.mybooksummaries.data.local.dao.BookDao
import com.decode.mybooksummaries.data.local.dao.MonthlyGoalDao
import com.decode.mybooksummaries.data.local.dao.QuoteDao
import com.decode.mybooksummaries.data.local.entity.BookEntity
import com.decode.mybooksummaries.data.local.entity.MonthlyGoalEntity
import com.decode.mybooksummaries.data.local.entity.QuoteEntity

@Database(
    entities = [BookEntity::class,QuoteEntity::class,MonthlyGoalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun quoteDao(): QuoteDao
    abstract fun monthlyGoalDao(): MonthlyGoalDao
}