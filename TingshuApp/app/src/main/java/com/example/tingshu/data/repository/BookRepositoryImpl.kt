package com.example.tingshu.data.repository

import com.example.tingshu.data.source.SourceManager
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.BookDetail
import com.example.tingshu.domain.model.Category
import com.example.tingshu.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val sourceManager: SourceManager
) : BookRepository {

    override fun searchBooks(keyword: String, page: Int, sourceId: String?): Flow<List<Book>> = flow {
        val books = if (sourceId != null) {
            sourceManager.search(sourceId, keyword, page)
        } else {
            sourceManager.searchAll(keyword, page)
        }
        emit(books)
    }

    override fun getBookDetail(bookId: String, sourceId: String): Flow<BookDetail?> = flow {
        val result = sourceManager.getBookDetail(sourceId, bookId)
        if (result != null) {
            val bookDetail = BookDetail(
                id = result.id,
                title = result.title,
                author = result.author,
                narrator = result.narrator,
                coverUrl = result.coverUrl,
                description = result.description,
                category = result.category,
                episodeCount = result.episodeCount,
                sourceId = sourceId,
                isCompleted = result.isCompleted,
                episodes = result.episodes
            )
            emit(bookDetail)
        } else {
            emit(null)
        }
    }

    override fun getRecommendations(sourceId: String?): Flow<List<Book>> = flow {
        val books = sourceManager.getRecommendations(sourceId)
        emit(books)
    }

    override fun getHotList(sourceId: String?): Flow<List<Book>> = flow {
        val books = sourceManager.getHotList(sourceId)
        emit(books)
    }

    override fun getCategories(sourceId: String?): Flow<List<Category>> = flow {
        val categories = mutableListOf<Category>()
        if (sourceId != null) {
            val source = sourceManager.getSourceById(sourceId)
            if (source != null) {
                try {
                    val sourceCategories = source.getCategories()
                    categories.addAll(
                        sourceCategories.map {
                            Category(
                                id = it.id,
                                name = it.name,
                                coverUrl = it.coverUrl,
                                sourceId = sourceId
                            )
                        }
                    )
                } catch (e: Exception) {
                }
            }
        } else {
            val allCategories = sourceManager.getAllCategories()
            for ((srcId, sourceCategories) in allCategories) {
                categories.addAll(
                    sourceCategories.map {
                        Category(
                            id = it.id,
                            name = it.name,
                            coverUrl = it.coverUrl,
                            sourceId = srcId
                        )
                    }
                )
            }
        }
        emit(categories)
    }

    override fun getCategoryBooks(categoryId: String, page: Int, sourceId: String): Flow<List<Book>> = flow {
        val books = sourceManager.getCategoryBooks(sourceId, categoryId, page)
        emit(books)
    }
}
