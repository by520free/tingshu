package com.example.tingshu.data.source

import com.example.tingshu.data.source.impl.LibriVoxSource
import com.example.tingshu.data.source.impl.XimalayaSource
import com.example.tingshu.domain.model.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceManager @Inject constructor(
    private val libriVoxSource: LibriVoxSource,
    private val ximalayaSource: XimalayaSource
) {

    private val sources = mutableListOf<AudioSource>()
    private val enabledSources = mutableSetOf<String>()

    init {
        registerSource(libriVoxSource)
        registerSource(ximalayaSource)
        enableSource(libriVoxSource.getSourceId())
    }

    fun registerSource(source: AudioSource) {
        if (sources.none { it.getSourceId() == source.getSourceId() }) {
            sources.add(source)
        }
    }

    fun unregisterSource(sourceId: String) {
        sources.removeAll { it.getSourceId() == sourceId }
        enabledSources.remove(sourceId)
    }

    fun enableSource(sourceId: String) {
        if (sources.any { it.getSourceId() == sourceId }) {
            enabledSources.add(sourceId)
        }
    }

    fun disableSource(sourceId: String) {
        enabledSources.remove(sourceId)
    }

    fun isSourceEnabled(sourceId: String): Boolean {
        return enabledSources.contains(sourceId)
    }

    fun getAllSources(): List<AudioSource> = sources.toList()

    fun getEnabledSources(): List<AudioSource> {
        return sources.filter { enabledSources.contains(it.getSourceId()) }
    }

    fun getSourceById(sourceId: String): AudioSource? {
        return sources.find { it.getSourceId() == sourceId }
    }

    suspend fun searchAll(keyword: String, page: Int): List<Book> {
        val results = mutableListOf<Book>()
        for (source in getEnabledSources()) {
            try {
                val books = source.search(keyword, page)
                results.addAll(books)
            } catch (e: Exception) {
                continue
            }
        }
        return results
    }

    suspend fun search(sourceId: String, keyword: String, page: Int): List<Book> {
        val source = getSourceById(sourceId) ?: return emptyList()
        return try {
            source.search(keyword, page)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getBookDetail(sourceId: String, bookId: String): BookDetailResult? {
        val source = getSourceById(sourceId) ?: return null
        return try {
            source.getBookDetail(bookId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllCategories(): Map<String, List<SourceCategory>> {
        val result = mutableMapOf<String, List<SourceCategory>>()
        for (source in getEnabledSources()) {
            try {
                val categories = source.getCategories()
                result[source.getSourceId()] = categories
            } catch (e: Exception) {
                continue
            }
        }
        return result
    }

    suspend fun getCategoryBooks(sourceId: String, categoryId: String, page: Int): List<Book> {
        val source = getSourceById(sourceId) ?: return emptyList()
        return try {
            source.getCategoryBooks(categoryId, page)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHotList(sourceId: String? = null): List<Book> {
        if (sourceId != null) {
            val source = getSourceById(sourceId) ?: return emptyList()
            return try {
                source.getHotList()
            } catch (e: Exception) {
                emptyList()
            }
        }
        
        val results = mutableListOf<Book>()
        for (source in getEnabledSources()) {
            try {
                val books = source.getHotList()
                results.addAll(books)
            } catch (e: Exception) {
                continue
            }
        }
        return results
    }

    suspend fun getRecommendations(sourceId: String? = null): List<Book> {
        if (sourceId != null) {
            val source = getSourceById(sourceId) ?: return emptyList()
            return try {
                source.getRecommendations()
            } catch (e: Exception) {
                emptyList()
            }
        }
        
        val results = mutableListOf<Book>()
        for (source in getEnabledSources()) {
            try {
                val books = source.getRecommendations()
                results.addAll(books)
            } catch (e: Exception) {
                continue
            }
        }
        return results
    }
}
