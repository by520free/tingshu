package com.example.tingshu.domain.model

data class BookDetail(
    val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val description: String,
    val category: String,
    val episodeCount: Int,
    val sourceId: String,
    val isCompleted: Boolean,
    val episodes: List<Episode>
)
