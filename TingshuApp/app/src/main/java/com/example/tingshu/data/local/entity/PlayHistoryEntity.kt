package com.example.tingshu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_history")
data class PlayHistoryEntity(
    @PrimaryKey val bookId: String,
    val bookTitle: String,
    val bookCover: String,
    val episodeId: String,
    val episodeTitle: String,
    val position: Long,
    val duration: Long,
    val lastPlayTime: Long,
    val sourceId: String
)
