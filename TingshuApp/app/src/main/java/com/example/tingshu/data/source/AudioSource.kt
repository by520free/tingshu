package com.example.tingshu.data.source

import com.example.tingshu.domain.model.Book

interface AudioSource {

    fun getSourceId(): String

    fun getSourceName(): String

    suspend fun search(keyword: String, page: Int): List<Book>

    suspend fun getBookDetail(bookId: String): BookDetailResult

    suspend fun getCategories(): List<SourceCategory>

    suspend fun getCategoryBooks(categoryId: String, page: Int): List<Book>

    suspend fun getHotList(): List<Book> = emptyList()

    suspend fun getRecommendations(): List<Book> = emptyList()
}
