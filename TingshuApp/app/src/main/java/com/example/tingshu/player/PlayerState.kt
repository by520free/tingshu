package com.example.tingshu.player

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode

data class PlayerState(
    val currentBook: Book? = null,
    val currentEpisode: Episode? = null,
    val episodes: List<Episode> = emptyList(),
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val isBuffering: Boolean = false,
    val currentEpisodeIndex: Int = 0
)
