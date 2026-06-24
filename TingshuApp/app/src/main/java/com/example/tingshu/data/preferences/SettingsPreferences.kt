package com.example.tingshu.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeMode {
    FOLLOW_SYSTEM, LIGHT, DARK
}

@Singleton
class SettingsPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore = context.dataStore

    val themeMode: Flow<ThemeMode> = dataStore.data
        .map { preferences ->
            val ordinal = preferences[Keys.THEME_MODE] ?: ThemeMode.FOLLOW_SYSTEM.ordinal
            ThemeMode.entries[ordinal]
        }

    val defaultPlaybackSpeed: Flow<Float> = dataStore.data
        .map { preferences ->
            preferences[Keys.DEFAULT_PLAYBACK_SPEED]?.toFloat() ?: 1.0f
        }

    val autoPlayNext: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Keys.AUTO_PLAY_NEXT] ?: true
        }

    val skipIntroSeconds: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[Keys.SKIP_INTRO_SECONDS] ?: 0
        }

    val skipOutroSeconds: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[Keys.SKIP_OUTRO_SECONDS] ?: 0
        }

    val sleepTimerDefault: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[Keys.SLEEP_TIMER_DEFAULT] ?: 30
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = mode.ordinal
        }
    }

    suspend fun setDefaultPlaybackSpeed(speed: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.DEFAULT_PLAYBACK_SPEED] = speed.toDouble()
        }
    }

    suspend fun setAutoPlayNext(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.AUTO_PLAY_NEXT] = enabled
        }
    }

    suspend fun setSkipIntroSeconds(seconds: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.SKIP_INTRO_SECONDS] = seconds
        }
    }

    suspend fun setSkipOutroSeconds(seconds: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.SKIP_OUTRO_SECONDS] = seconds
        }
    }

    suspend fun setSleepTimerDefault(minutes: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.SLEEP_TIMER_DEFAULT] = minutes
        }
    }

    private object Keys {
        val THEME_MODE = intPreferencesKey("theme_mode")
        val DEFAULT_PLAYBACK_SPEED = doublePreferencesKey("default_playback_speed")
        val AUTO_PLAY_NEXT = booleanPreferencesKey("auto_play_next")
        val SKIP_INTRO_SECONDS = intPreferencesKey("skip_intro_seconds")
        val SKIP_OUTRO_SECONDS = intPreferencesKey("skip_outro_seconds")
        val SLEEP_TIMER_DEFAULT = intPreferencesKey("sleep_timer_default")
    }
}
