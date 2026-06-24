package com.example.tingshu.domain.usecase

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(sourceId: String? = null): Flow<List<Book>> {
        return bookRepository.getRecommendations(sourceId)
    }
}
