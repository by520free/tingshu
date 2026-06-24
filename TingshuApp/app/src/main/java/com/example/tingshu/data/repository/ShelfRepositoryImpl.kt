package com.example.tingshu.data.repository

import com.example.tingshu.data.local.dao.FavoriteDao
import com.example.tingshu.data.local.dao.PlayHistoryDao
import com.example.tingshu.data.local.dao.PlayProgressDao
import com.example.tingshu.data.local.entity.FavoriteEntity
import com.example.tingshu.data.local.entity.PlayHistoryEntity
import com.example.tingshu.data.local.entity.PlayProgressEntity
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.PlayHistoryItem
import com.example.tingshu.domain.model.PlayProgress
import com.example.tingshu.domain.repository.ShelfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShelfRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val playHistoryDao: PlayHistoryDao,
    private val playProgressDao: PlayProgressDao
) : ShelfRepository {

    override fun getFavorites(): Flow<List<Book>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                Book(
                    id = entity.bookId,
                    title = entity.title,
                    author = entity.author,
                    narrator = entity.narrator,
                    coverUrl = entity.coverUrl,
                    description = entity.description,
                    category = entity.category,
                    episodeCount = entity.episodeCount,
                    sourceId = entity.sourceId,
                    isCompleted = entity.isCompleted
                )
            }
        }
    }

    override suspend fun addFavorite(book: Book) {
        val favoriteEntity = FavoriteEntity(
            bookId = book.id,
            title = book.title,
            author = book.author,
            narrator = book.narrator,
            coverUrl = book.coverUrl,
            description = book.description,
            category = book.category,
            episodeCount = book.episodeCount,
            sourceId = book.sourceId,
            isCompleted = book.isCompleted
        )
        favoriteDao.insertFavorite(favoriteEntity)
    }

    override suspend fun removeFavorite(bookId: String) {
        favoriteDao.deleteFavoriteById(bookId)
    }

    override fun isFavorite(bookId: String): Flow<Boolean> {
        return favoriteDao.isFavorite(bookId)
    }

    override fun getPlayHistory(): Flow<List<PlayHistoryItem>> {
        return playHistoryDao.getAllPlayHistory().map { entities ->
            entities.map { entity ->
                PlayHistoryItem(
                    bookId = entity.bookId,
                    bookTitle = entity.bookTitle,
                    bookCover = entity.bookCover,
                    episodeId = entity.episodeId,
                    episodeTitle = entity.episodeTitle,
                    position = entity.position,
                    duration = entity.duration,
                    lastPlayTime = entity.lastPlayTime,
                    sourceId = entity.sourceId
                )
            }
        }
    }

    override suspend fun savePlayProgress(
        bookId: String,
        episodeId: String,
        position: Long,
        duration: Long,
        bookTitle: String,
        bookCover: String,
        episodeTitle: String,
        sourceId: String
    ) {
        val progressEntity = PlayProgressEntity(
            bookId = bookId,
            episodeId = episodeId,
            position = position,
            duration = duration
        )
        playProgressDao.insertPlayProgress(progressEntity)

        val historyEntity = PlayHistoryEntity(
            bookId = bookId,
            bookTitle = bookTitle,
            bookCover = bookCover,
            episodeId = episodeId,
            episodeTitle = episodeTitle,
            position = position,
            duration = duration,
            lastPlayTime = System.currentTimeMillis(),
            sourceId = sourceId
        )
        playHistoryDao.insertPlayHistory(historyEntity)
    }

    override fun getPlayProgress(bookId: String): Flow<PlayProgress?> {
        return playProgressDao.getPlayProgress(bookId).map { entity ->
            entity?.let {
                PlayProgress(
                    bookId = it.bookId,
                    episodeId = it.episodeId,
                    position = it.position,
                    duration = it.duration,
                    lastUpdateTime = it.lastUpdateTime
                )
            }
        }
    }
}
