package com.vibespace.app.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.repository.MusicRepository
import com.vibespace.app.data.repository.RecentlyPlayedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    recentlyPlayedRepository: RecentlyPlayedRepository
) : ViewModel() {

    val recentlyPlayed: StateFlow<List<TrackEntity>> = recentlyPlayedRepository.recentlyPlayed
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uploadedTracks: StateFlow<List<TrackEntity>> = musicRepository.uploadedTracks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _uploadMessage = MutableStateFlow<String?>(null)
    val uploadMessage: StateFlow<String?> = _uploadMessage.asStateFlow()

    fun clearUploadMessage() {
        _uploadMessage.value = null
    }

    fun uploadSongFiles(uris: List<Uri>) {
        if (uris.isEmpty()) return
        viewModelScope.launch {
            _isUploading.value = true
            try {
                val imported = musicRepository.importAudioFiles(uris)
                if (imported.isNotEmpty()) {
                    val count = imported.size
                    _uploadMessage.value = "Added $count song${if (count > 1) "s" else ""} to library"
                } else {
                    _uploadMessage.value = "Could not import the selected audio file(s)"
                }
            } catch (e: Exception) {
                _uploadMessage.value = "Upload failed: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isUploading.value = false
            }
        }
    }

    fun deleteUploadedTrack(track: TrackEntity) {
        viewModelScope.launch {
            musicRepository.deleteTrack(track)
        }
    }

    fun scanMusicIfEmpty() {
        viewModelScope.launch {
            if (musicRepository.isCacheEmpty()) {
                scanMusic()
            }
        }
    }

    fun scanMusic() {
        viewModelScope.launch {
            _isScanning.value = true
            try {
                musicRepository.scanAndCacheMusic()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isScanning.value = false
            }
        }
    }
}
