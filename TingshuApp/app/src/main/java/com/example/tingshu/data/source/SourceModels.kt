package com.example.tingshu.data.source

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode

data class SourceCategory(
    val id: String,
    val name: String,
    val coverUrl: String = ""
)

data class BookDetailResult(
    val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val coverUrl: String,
    val description: String,
    val category: String,
    val episodeCount: Int,
    val isCompleted: Boolean,
    val episodes: List<Episode>
)

sealed class SourceResult<out T> {
    data class Success<out T>(val data: T) : SourceResult<T>()
    data class Error(val exception: Exception) : SourceResult<Nothing>()
    object Loading : SourceResult<Nothing>()
}
