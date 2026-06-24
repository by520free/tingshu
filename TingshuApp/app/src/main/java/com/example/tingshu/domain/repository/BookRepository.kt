package com.example.tingshu.domain.repository

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.BookDetail
import com.example.tingshu.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun searchBooks(keyword: String, page: Int, sourceId: String? = null): Flow<List<Book>>

    fun getBookDetail(bookId: String, sourceId: String): Flow<BookDetail?>

    fun getRecommendations(sourceId: String? = null): Flow<List<Book>>

    fun getHotList(sourceId: String? = null): Flow<List<Book>>

    fun getCategories(sourceId: String? = null): Flow<List<Category>>

    fun getCategoryBooks(categoryId: String, page: Int, sourceId: String): Flow<List<Book>>
}
