package com.example.tingshu.domain.model

data class PlayProgress(
    val bookId: String,
    val episodeId: String,
    val position: Long,
    val duration: Long,
    val lastUpdateTime: Long
)
