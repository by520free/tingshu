package com.example.tingshu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey val keyword: String,
    val searchTime: Long = System.currentTimeMillis()
)
