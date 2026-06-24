package com.example.tingshu.domain.usecase

import com.example.tingshu.domain.model.BookDetail
import com.example.tingshu.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke(
        bookId: String,
        sourceId: String
    ): Flow<BookDetail?> {
        return bookRepository.getBookDetail(bookId, sourceId)
    }
}
