package com.example.tingshu.data.source.impl

import com.example.tingshu.data.source.AudioSource
import com.example.tingshu.data.source.BookDetailResult
import com.example.tingshu.data.source.SourceCategory
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibriVoxSource @Inject constructor() : AudioSource {

    private val baseUrl = "https://librivox.org"

    override fun getSourceId(): String = "librivox"

    override fun getSourceName(): String = "LibriVox"

    override suspend fun search(keyword: String, page: Int): List<Book> {
        val searchUrl = "$baseUrl/search?title=$keyword&page=$page"
        return try {
            val doc = Jsoup.connect(searchUrl).get()
            parseBookList(doc)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getBookDetail(bookId: String): BookDetailResult {
        val bookUrl = "$baseUrl/$bookId"
        val doc = Jsoup.connect(bookUrl).get()
        return parseBookDetail(doc, bookId)
    }

    override suspend fun getCategories(): List<SourceCategory> {
        return listOf(
            SourceCategory("fiction", "Fiction"),
            SourceCategory("non-fiction", "Non-Fiction"),
            SourceCategory("poetry", "Poetry"),
            SourceCategory("drama", "Drama"),
            SourceCategory("children", "Children"),
            SourceCategory("history", "History"),
            SourceCategory("biography", "Biography"),
            SourceCategory("philosophy", "Philosophy"),
            SourceCategory("science", "Science"),
            SourceCategory("fantasy", "Fantasy")
        )
    }

    override suspend fun getCategoryBooks(categoryId: String, page: Int): List<Book> {
        val categoryUrl = "$baseUrl/genre/$categoryId?page=$page"
        return try {
            val doc = Jsoup.connect(categoryUrl).get()
            parseBookList(doc)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getHotList(): List<Book> {
        val popularUrl = "$baseUrl/most_popular"
        return try {
            val doc = Jsoup.connect(popularUrl).get()
            parseBookList(doc)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getRecommendations(): List<Book> {
        val newUrl = "$baseUrl/new_release"
        return try {
            val doc = Jsoup.connect(newUrl).get()
            parseBookList(doc)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseBookList(doc: Document): List<Book> {
        val books = mutableListOf<Book>()
        val bookElements = doc.select(".book-list-item, .result-item, article.book")
        
        for (element in bookElements) {
            try {
                val titleElement = element.select("h3 a, .book-title a, .title a").first()
                val title = titleElement?.text() ?: continue
                val bookUrl = titleElement.attr("href")
                val bookId = bookUrl.removePrefix(baseUrl).trim('/')
                
                val author = element.select(".book-author, .author, .creator").first()?.text() ?: "Unknown"
                val narrator = element.select(".book-narrator, .narrator, .reader").first()?.text() ?: "Unknown"
                val coverUrl = element.select("img.book-cover, img.cover, .cover img").first()?.attr("src") ?: ""
                val description = element.select(".book-description, .description, p.desc").first()?.text() ?: ""
                val category = element.select(".book-genre, .genre, .category").first()?.text() ?: ""
                val episodeCountText = element.select(".chapters, .num_sections, .duration").first()?.text() ?: "0"
                val episodeCount = episodeCountText.filter { it.isDigit() }.toIntOrNull() ?: 0
                val isCompleted = element.select(".completed, .complete").isNotEmpty() || 
                                  !element.select(".status").text().contains("in progress", true)

                books.add(
                    Book(
                        id = bookId,
                        title = title,
                        author = author,
                        narrator = narrator,
                        coverUrl = coverUrl,
                        description = description,
                        category = category,
                        episodeCount = episodeCount,
                        sourceId = getSourceId(),
                        isCompleted = isCompleted
                    )
                )
            } catch (e: Exception) {
                continue
            }
        }
        
        return books
    }

    private fun parseBookDetail(doc: Document, bookId: String): BookDetailResult {
        val title = doc.select("h1.book-title, .product-title, h1").first()?.text() ?: ""
        val author = doc.select(".book-author .author-name, .author a, .creator").first()?.text() ?: "Unknown"
        val narrator = doc.select(".book-narrator .narrator-name, .narrator a, .reader").first()?.text() ?: "Unknown"
        val coverUrl = doc.select("img.book-cover, .book-cover img, #cover img").first()?.attr("src") ?: ""
        val description = doc.select(".book-description, .product-description, .description").text() ?: ""
        val category = doc.select(".book-genre a, .genre a, .category a").first()?.text() ?: ""
        val episodeCount = doc.select(".chapter-list li, .track-list li, section.chapter").size
        val isCompleted = !doc.select(".status").text().contains("in progress", true)

        val episodes = parseEpisodes(doc)

        return BookDetailResult(
            id = bookId,
            title = title,
            author = author,
            narrator = narrator,
            coverUrl = coverUrl,
            description = description,
            category = category,
            episodeCount = episodeCount,
            isCompleted = isCompleted,
            episodes = episodes
        )
    }

    private fun parseEpisodes(doc: Document): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val episodeElements = doc.select(".chapter-list li, .track-list li, section.chapter, table.chapters tr")
        
        for ((index, element) in episodeElements.withIndex()) {
            try {
                val titleElement = element.select("a.chapter-name, .track-title a, .chapter a, td.chapter a").first()
                val title = titleElement?.text() ?: "Chapter ${index + 1}"
                val chapterUrl = titleElement?.attr("href") ?: ""
                val audioUrl = element.select("a[href$=.mp3], audio source").first()?.attr("href") ?: ""
                val durationText = element.select(".duration, .length, td.duration").first()?.text() ?: "0:00"
                val duration = parseDuration(durationText)

                val episodeId = chapterUrl.ifEmpty { "chapter_$index" }

                episodes.add(
                    Episode(
                        id = episodeId,
                        title = title,
                        url = chapterUrl,
                        audioUrl = audioUrl,
                        duration = duration,
                        index = index
                    )
                )
            } catch (e: Exception) {
                continue
            }
        }
        
        return episodes
    }

    private fun parseDuration(durationStr: String): Long {
        val parts = durationStr.split(":")
        return when (parts.size) {
            2 -> {
                val minutes = parts[0].toLongOrNull() ?: 0
                val seconds = parts[1].toLongOrNull() ?: 0
                (minutes * 60 + seconds) * 1000
            }
            3 -> {
                val hours = parts[0].toLongOrNull() ?: 0
                val minutes = parts[1].toLongOrNull() ?: 0
                val seconds = parts[2].toLongOrNull() ?: 0
                (hours * 3600 + minutes * 60 + seconds) * 1000
            }
            else -> 0L
        }
    }
}
