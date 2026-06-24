package com.example.tingshu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val bookId: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val description: String,
    val category: String,
    val episodeCount: Int,
    val sourceId: String,
    val isCompleted: Boolean,
    val addTime: Long = System.currentTimeMillis()
)
