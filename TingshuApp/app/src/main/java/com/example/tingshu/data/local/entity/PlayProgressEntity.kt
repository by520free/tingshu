package com.example.tingshu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play_progress")
data class PlayProgressEntity(
    @PrimaryKey val bookId: String,
    val episodeId: String,
    val position: Long,
    val duration: Long,
    val lastUpdateTime: Long = System.currentTimeMillis()
)
