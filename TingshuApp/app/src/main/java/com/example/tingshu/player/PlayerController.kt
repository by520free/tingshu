package com.example.tingshu.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.tingshu.domain.model.Book
import com.example.tingshu.domain.model.Episode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressUpdateJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    _playerState.value = _playerState.value.copy(isBuffering = true)
                }
                Player.STATE_READY -> {
                    _playerState.value = _playerState.value.copy(
                        isBuffering = false,
                        duration = exoPlayer?.duration ?: 0L
                    )
                }
                Player.STATE_IDLE, Player.STATE_ENDED -> {
                    _playerState.value = _playerState.value.copy(isBuffering = false)
                    if (playbackState == Player.STATE_ENDED) {
                        next()
                    }
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playerState.value = _playerState.value.copy(isPlaying = isPlaying)
            if (isPlaying) {
                startProgressUpdate()
            } else {
                stopProgressUpdate()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            _playerState.value = _playerState.value.copy(isBuffering = false, isPlaying = false)
        }
    }

    private fun ensurePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                addListener(playerListener)
                setPlaybackSpeed(_playerState.value.playbackSpeed)
            }
        }
    }

    fun playBook(book: Book, episodes: List<Episode>, startEpisodeIndex: Int = 0) {
        ensurePlayer()
        val player = exoPlayer ?: return

        val startIdx = startEpisodeIndex.coerceIn(0, episodes.size - 1)
        val startEpisode = episodes[startIdx]

        _playerState.value = _playerState.value.copy(
            currentBook = book,
            episodes = episodes,
            currentEpisode = startEpisode,
            currentEpisodeIndex = startIdx,
            currentPosition = 0L,
            duration = 0L
        )

        val mediaItems = episodes.map { episode ->
            MediaItem.fromUri(episode.audioUrl)
        }
        player.setMediaItems(mediaItems, startIdx, 0L)
        player.prepare()
        player.play()
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun toggle() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playerState.value = _playerState.value.copy(currentPosition = positionMs)
    }

    fun next() {
        val currentIndex = _playerState.value.currentEpisodeIndex
        val episodes = _playerState.value.episodes
        if (currentIndex < episodes.size - 1) {
            val nextIndex = currentIndex + 1
            exoPlayer?.seekToNextMediaItem()
            _playerState.value = _playerState.value.copy(
                currentEpisodeIndex = nextIndex,
                currentEpisode = episodes[nextIndex],
                currentPosition = 0L
            )
        }
    }

    fun previous() {
        val currentIndex = _playerState.value.currentEpisodeIndex
        val episodes = _playerState.value.episodes
        if (currentIndex > 0) {
            val prevIndex = currentIndex - 1
            exoPlayer?.seekToPreviousMediaItem()
            _playerState.value = _playerState.value.copy(
                currentEpisodeIndex = prevIndex,
                currentEpisode = episodes[prevIndex],
                currentPosition = 0L
            )
        } else {
            exoPlayer?.seekTo(0L)
        }
    }

    fun setSpeed(speed: Float) {
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
        exoPlayer?.setPlaybackSpeed(speed)
    }

    fun playEpisode(episodeIndex: Int) {
        val episodes = _playerState.value.episodes
        if (episodeIndex in episodes.indices) {
            exoPlayer?.seekToDefaultPosition(episodeIndex)
            _playerState.value = _playerState.value.copy(
                currentEpisodeIndex = episodeIndex,
                currentEpisode = episodes[episodeIndex],
                currentPosition = 0L
            )
            exoPlayer?.play()
        }
    }

    private fun startProgressUpdate() {
        stopProgressUpdate()
        progressUpdateJob = scope.launch {
            while (exoPlayer?.isPlaying == true) {
                val position = exoPlayer?.currentPosition ?: 0L
                _playerState.value = _playerState.value.copy(currentPosition = position)
                delay(500)
            }
        }
    }

    private fun stopProgressUpdate() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    fun release() {
        stopProgressUpdate()
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
    }
}
