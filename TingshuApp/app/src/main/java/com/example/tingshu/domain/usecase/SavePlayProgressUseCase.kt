package com.example.tingshu.domain.usecase

import com.example.tingshu.domain.repository.ShelfRepository
import javax.inject.Inject

class SavePlayProgressUseCase @Inject constructor(
    private val shelfRepository: ShelfRepository
) {
    suspend operator fun invoke(
        bookId: String,
        episodeId: String,
        position: Long,
        duration: Long = 0L,
        bookTitle: String = "",
        bookCover: String = "",
        episodeTitle: String = "",
        sourceId: String = ""
    ) {
        shelfRepository.savePlayProgress(
            bookId = bookId,
            episodeId = episodeId,
            position = position,
            duration = duration,
            bookTitle = bookTitle,
            bookCover = bookCover,
            episodeTitle = episodeTitle,
            sourceId = sourceId
        )
    }
}
