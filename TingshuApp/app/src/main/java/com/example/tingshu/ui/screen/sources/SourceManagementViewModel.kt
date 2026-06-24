package com.example.tingshu.ui.screen.sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingshu.data.source.AudioSource
import com.example.tingshu.data.source.SourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SourceUiState(
    val enabledSources: List<AudioSource> = emptyList(),
    val disabledSources: List<AudioSource> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SourceManagementViewModel @Inject constructor(
    private val sourceManager: SourceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SourceUiState())
    val uiState: StateFlow<SourceUiState> = _uiState.asStateFlow()

    init {
        loadSources()
    }

    private fun loadSources() {
        val allSources = sourceManager.getAllSources()
        val enabled = allSources.filter { sourceManager.isSourceEnabled(it.getSourceId()) }
        val disabled = allSources.filter { !sourceManager.isSourceEnabled(it.getSourceId()) }
        _uiState.value = SourceUiState(
            enabledSources = enabled,
            disabledSources = disabled,
            isLoading = false
        )
    }

    fun toggleSource(sourceId: String, enabled: Boolean) {
        if (enabled) {
            sourceManager.enableSource(sourceId)
        } else {
            sourceManager.disableSource(sourceId)
        }
        loadSources()
    }
}
