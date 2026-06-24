package com.example.tingshu.domain.repository

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.PlayHistoryItem
import com.example.tingshu.domain.model.PlayProgress
import kotlinx.coroutines.flow.Flow

interface ShelfRepository {

    fun getFavorites(): Flow<List<Book>>

    suspend fun addFavorite(book: Book)

    suspend fun removeFavorite(bookId: String)

    fun isFavorite(bookId: String): Flow<Boolean>

    fun getPlayHistory(): Flow<List<PlayHistoryItem>>

    suspend fun savePlayProgress(
        bookId: String,
        episodeId: String,
        position: Long,
        duration: Long = 0L,
        bookTitle: String = "",
        bookCover: String = "",
        episodeTitle: String = "",
        sourceId: String = ""
    )

    fun getPlayProgress(bookId: String): Flow<PlayProgress?>
}
