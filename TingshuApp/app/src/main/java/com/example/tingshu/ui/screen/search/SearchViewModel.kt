package com.example.tingshu.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingshu.domain.model.Book
import com.example.tingshu.ui.screen.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val searchQuery: String = "",
    val searchHistory: List<String> = emptyList(),
    val hotSearchKeywords: List<String> = emptyList(),
    val searchResults: List<Book> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                hotSearchKeywords = listOf(
                    "凡人修仙传", "斗破苍穹", "庆余年", "全职高手",
                    "鬼吹灯", "盗墓笔记", "三体", "明朝那些事儿"
                ),
                searchHistory = listOf("斗破苍穹", "三体")
            )
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onSearch(query: String = _uiState.value.searchQuery) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSearching = true,
                hasSearched = true,
                searchQuery = query
            )

            val results = HomeViewModel.sampleBooks.filter { book ->
                book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true) ||
                        book.narrator.contains(query, ignoreCase = true)
            }

            val currentHistory = _uiState.value.searchHistory.toMutableList()
            currentHistory.remove(query)
            currentHistory.add(0, query)
            if (currentHistory.size > 10) {
                currentHistory.removeLast()
            }

            _uiState.value = _uiState.value.copy(
                searchResults = results,
                isSearching = false,
                searchHistory = currentHistory
            )
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList(),
            hasSearched = false
        )
    }

    fun clearSearchHistory() {
        _uiState.value = _uiState.value.copy(searchHistory = emptyList())
    }

    fun removeFromHistory(keyword: String) {
        val currentHistory = _uiState.value.searchHistory.toMutableList()
        currentHistory.remove(keyword)
        _uiState.value = _uiState.value.copy(searchHistory = currentHistory)
    }
}
