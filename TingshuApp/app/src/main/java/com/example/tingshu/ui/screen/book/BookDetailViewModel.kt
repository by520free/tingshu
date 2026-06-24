package com.example.tingshu.ui.screen.book

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode
import com.example.tingshu.player.PlayerController
import com.example.tingshu.ui.screen.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookDetailUiState(
    val book: Book? = null,
    val episodes: List<Episode> = emptyList(),
    val isFavorite: Boolean = false,
    val isDescriptionExpanded: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController
) : ViewModel() {

    private val bookId: String = checkNotNull(savedStateHandle["bookId"])

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    init {
        loadBookDetail()
    }

    private fun loadBookDetail() {
        viewModelScope.launch {
            val book = HomeViewModel.sampleBooks.find { it.id == bookId }
            val episodes = (1..(book?.episodeCount ?: 100)).map { index ->
                Episode(
                    id = "ep_$index",
                    title = "第${index}章 精彩内容",
                    url = "https://example.com/ep/$index",
                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-${(index % 16) + 1}.mp3",
                    duration = (15 * 60 + index * 30).toLong(),
                    index = index
                )
            }

            _uiState.value = BookDetailUiState(
                book = book,
                episodes = episodes,
                isLoading = false
            )
        }
    }

    fun toggleFavorite() {
        _uiState.value = _uiState.value.copy(
            isFavorite = !_uiState.value.isFavorite
        )
    }

    fun toggleDescriptionExpanded() {
        _uiState.value = _uiState.value.copy(
            isDescriptionExpanded = !_uiState.value.isDescriptionExpanded
        )
    }

    fun playEpisode(episodeId: String) {
        val state = _uiState.value
        val book = state.book ?: return
        val episodeIndex = state.episodes.indexOfFirst { it.id == episodeId }
        if (episodeIndex >= 0) {
            playerController.playBook(book, state.episodes, episodeIndex)
        }
    }

    fun playAll() {
        val state = _uiState.value
        val book = state.book ?: return
        playerController.playBook(book, state.episodes, 0)
    }
}
