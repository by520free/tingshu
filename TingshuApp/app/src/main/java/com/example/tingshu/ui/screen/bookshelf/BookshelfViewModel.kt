package com.example.tingshu.ui.screen.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.PlayHistoryItem
import com.example.tingshu.domain.repository.ShelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookshelfUiState(
    val favorites: List<Book> = emptyList(),
    val downloads: List<Book> = emptyList(),
    val playHistory: List<PlayHistoryItem> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    private val shelfRepository: ShelfRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookshelfUiState())
    val uiState: StateFlow<BookshelfUiState> = _uiState.asStateFlow()

    init {
        loadShelfData()
    }

    private fun loadShelfData() {
        viewModelScope.launch {
            shelfRepository.getFavorites().collect { favorites ->
                _uiState.value = _uiState.value.copy(
                    favorites = favorites,
                    isLoading = false
                )
            }
        }
        viewModelScope.launch {
            shelfRepository.getPlayHistory().collect { history ->
                _uiState.value = _uiState.value.copy(
                    playHistory = history
                )
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    fun removeFavorite(bookId: String) {
        viewModelScope.launch {
            shelfRepository.removeFavorite(bookId)
        }
    }

    fun clearPlayHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(playHistory = emptyList())
        }
    }
}
