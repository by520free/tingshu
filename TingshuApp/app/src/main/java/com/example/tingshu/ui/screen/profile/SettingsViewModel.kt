package com.example.tingshu.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingshu.data.local.dao.SearchHistoryDao
import com.example.tingshu.data.preferences.SettingsPreferences
import com.example.tingshu.data.preferences.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.FOLLOW_SYSTEM,
    val defaultPlaybackSpeed: Float = 1.0f,
    val autoPlayNext: Boolean = true,
    val skipIntroSeconds: Int = 0,
    val skipOutroSeconds: Int = 0,
    val sleepTimerDefault: Int = 30,
    val cacheSize: String = "0 MB"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsPreferences.themeMode.collect { mode ->
                _uiState.value = _uiState.value.copy(themeMode = mode)
            }
        }
        viewModelScope.launch {
            settingsPreferences.defaultPlaybackSpeed.collect { speed ->
                _uiState.value = _uiState.value.copy(defaultPlaybackSpeed = speed)
            }
        }
        viewModelScope.launch {
            settingsPreferences.autoPlayNext.collect { enabled ->
                _uiState.value = _uiState.value.copy(autoPlayNext = enabled)
            }
        }
        viewModelScope.launch {
            settingsPreferences.skipIntroSeconds.collect { seconds ->
                _uiState.value = _uiState.value.copy(skipIntroSeconds = seconds)
            }
        }
        viewModelScope.launch {
            settingsPreferences.skipOutroSeconds.collect { seconds ->
                _uiState.value = _uiState.value.copy(skipOutroSeconds = seconds)
            }
        }
        viewModelScope.launch {
            settingsPreferences.sleepTimerDefault.collect { minutes ->
                _uiState.value = _uiState.value.copy(sleepTimerDefault = minutes)
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsPreferences.setThemeMode(mode)
        }
    }

    fun setDefaultPlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            settingsPreferences.setDefaultPlaybackSpeed(speed)
        }
    }

    fun setAutoPlayNext(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setAutoPlayNext(enabled)
        }
    }

    fun setSkipIntroSeconds(seconds: Int) {
        viewModelScope.launch {
            settingsPreferences.setSkipIntroSeconds(seconds)
        }
    }

    fun setSkipOutroSeconds(seconds: Int) {
        viewModelScope.launch {
            settingsPreferences.setSkipOutroSeconds(seconds)
        }
    }

    fun setSleepTimerDefault(minutes: Int) {
        viewModelScope.launch {
            settingsPreferences.setSleepTimerDefault(minutes)
        }
    }

    fun clearCache() {
        _uiState.value = _uiState.value.copy(cacheSize = "0 MB")
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.clearAllSearchHistory()
        }
    }
}
