package com.example.tingshu.domain.usecase

import com.example.tingshu.domain.model.PlayHistoryItem
import com.example.tingshu.domain.repository.ShelfRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlayHistoryUseCase @Inject constructor(
    private val shelfRepository: ShelfRepository
) {
    operator fun invoke(): Flow<List<PlayHistoryItem>> {
        return shelfRepository.getPlayHistory()
    }
}
