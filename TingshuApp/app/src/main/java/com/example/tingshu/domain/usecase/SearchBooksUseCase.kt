package com.example.tingshu.domain.usecase

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(
        keyword: String,
        page: Int,
        sourceId: String? = null
    ): Flow<List<Book>> {
        return bookRepository.searchBooks(keyword, page, sourceId)
    }
}
