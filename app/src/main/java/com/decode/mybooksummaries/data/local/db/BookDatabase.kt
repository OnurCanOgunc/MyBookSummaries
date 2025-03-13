package com.decode.mybooksummaries.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.decode.mybooksummaries.data.local.dao.BookDao
import com.decode.mybooksummaries.data.local.entity.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}