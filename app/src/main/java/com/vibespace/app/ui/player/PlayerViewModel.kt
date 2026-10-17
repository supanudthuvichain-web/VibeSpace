package com.vibespace.app.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.repository.QueueRepository
import com.vibespace.app.data.repository.RecentlyPlayedRepository
import com.vibespace.app.player.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController,
    private val queueRepository: QueueRepository,
    private val recentlyPlayedRepository: RecentlyPlayedRepository
) : ViewModel() {

    val isPlaying: StateFlow<Boolean> = playerController.isPlaying
    val currentTrack: StateFlow<TrackEntity?> = playerController.currentTrack
    val duration: StateFlow<Long> = playerController.duration
    val shuffleEnabled: StateFlow<Boolean> = playerController.shuffleEnabled
    val repeatMode: StateFlow<Int> = playerController.repeatMode
    val hasNext: StateFlow<Boolean> = playerController.hasNext
    val hasPrevious: StateFlow<Boolean> = playerController.hasPrevious

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    init {
        playerController.connect()

        // Periodic ticker for playback position and persisting progress
        viewModelScope.launch {
            while (isActive) {
                if (playerController.isPlaying.value) {
                    val pos = playerController.getCurrentPosition()
                    _currentPosition.value = pos
                    queueRepository.updatePosition(pos)
                }
                delay(500)
            }
        }

        // Record recently played and save current track in DB
        viewModelScope.launch {
            playerController.currentTrack.collect { track ->
                track?.let {
                    recentlyPlayedRepository.recordTrackPlayed(it.id)
                    queueRepository.setCurrentTrack(it.id)
                }
            }
        }
    }

    fun playTrack(track: TrackEntity, playlist: List<TrackEntity> = listOf(track)) {
        val startIndex = playlist.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        playerController.playTracks(playlist, startIndex)
        viewModelScope.launch {
            queueRepository.replaceQueue(playlist.map { it.id }, track.id)
        }
    }

    fun togglePlayPause() {
        playerController.togglePlayPause()
    }

    fun skipToNext() {
        playerController.skipToNext()
    }

    fun skipToPrevious() {
        playerController.skipToPrevious()
    }

    fun seekTo(positionMs: Long) {
        _currentPosition.value = positionMs
        playerController.seekTo(positionMs)
    }

    fun toggleShuffle() {
        playerController.toggleShuffle()
        viewModelScope.launch {
            queueRepository.updateShuffle(playerController.shuffleEnabled.value)
        }
    }

    fun cycleRepeatMode() {
        playerController.cycleRepeatMode()
        viewModelScope.launch {
            queueRepository.updateRepeatMode(playerController.repeatMode.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerController.disconnect()
    }
}
