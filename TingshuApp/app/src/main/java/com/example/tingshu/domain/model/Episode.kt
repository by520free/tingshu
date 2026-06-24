package com.example.tingshu.domain.model

data class Episode(
    val id: String,
    val title: String,
    val url: String,
    val audioUrl: String,
    val duration: Long,
    val index: Int
)
