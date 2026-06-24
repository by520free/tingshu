package com.example.tingshu.domain.usecase

import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.repository.ShelfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val shelfRepository: ShelfRepository
) {
    suspend operator fun invoke(book: Book): Boolean {
        val isCurrentlyFavorite = shelfRepository.isFavorite(book.id).first()
        return if (isCurrentlyFavorite) {
            shelfRepository.removeFavorite(book.id)
            false
        } else {
            shelfRepository.addFavorite(book)
            true
        }
    }
}
