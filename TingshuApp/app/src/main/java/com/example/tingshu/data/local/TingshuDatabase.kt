package com.example.tingshu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tingshu.data.local.dao.BookDao
import com.example.tingshu.data.local.dao.ChapterDao
import com.example.tingshu.data.local.dao.FavoriteDao
import com.example.tingshu.data.local.dao.PlayHistoryDao
import com.example.tingshu.data.local.dao.PlayProgressDao
import com.example.tingshu.data.local.dao.SearchHistoryDao
import com.example.tingshu.data.local.entity.BookEntity
import com.example.tingshu.data.local.entity.ChapterEntity
import com.example.tingshu.data.local.entity.FavoriteEntity
import com.example.tingshu.data.local.entity.PlayHistoryEntity
import com.example.tingshu.data.local.entity.PlayProgressEntity
import com.example.tingshu.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        FavoriteEntity::class,
        PlayHistoryEntity::class,
        SearchHistoryEntity::class,
        PlayProgressEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TingshuDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun playHistoryDao(): PlayHistoryDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun playProgressDao(): PlayProgressDao
}
