package com.example.tingshu.data.source.impl

import com.example.tingshu.data.source.AudioSource
import com.example.tingshu.data.source.BookDetailResult
import com.example.tingshu.data.source.SourceCategory
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XimalayaSource @Inject constructor() : AudioSource {

    override fun getSourceId(): String = "ximalaya"

    override fun getSourceName(): String = "喜马拉雅"

    override suspend fun search(keyword: String, page: Int): List<Book> {
        return emptyList()
    }

    override suspend fun getBookDetail(bookId: String): BookDetailResult {
        return BookDetailResult(
            id = bookId,
            title = "",
            author = "",
            narrator = "",
            coverUrl = "",
            description = "",
            category = "",
            episodeCount = 0,
            isCompleted = false,
            episodes = emptyList()
        )
    }

    override suspend fun getCategories(): List<SourceCategory> {
        return emptyList()
    }

    override suspend fun getCategoryBooks(categoryId: String, page: Int): List<Book> {
        return emptyList()
    }

    override suspend fun getHotList(): List<Book> {
        return emptyList()
    }

    override suspend fun getRecommendations(): List<Book> {
        return emptyList()
    }
}
