package com.example.tingshu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val lastChapter: String,
    val lastReadTime: Long = System.currentTimeMillis(),
    val currentChapter: Int = 0,
    val currentPosition: Long = 0,
    val sourceUrl: String
)
