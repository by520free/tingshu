package com.example.tingshu.domain.model

data class PlayHistoryItem(
    val bookId: String,
    val bookTitle: String,
    val bookCover: String,
    val episodeId: String,
    val episodeTitle: String,
    val position: Long,
    val duration: Long,
    val lastPlayTime: Long,
    val sourceId: String
)
