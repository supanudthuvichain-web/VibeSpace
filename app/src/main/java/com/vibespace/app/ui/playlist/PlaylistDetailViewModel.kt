package com.vibespace.app.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibespace.app.data.db.entity.PlaylistEntity
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _playlistId = MutableStateFlow<Long?>(null)

    val playlistTracks: StateFlow<List<TrackEntity>> = _playlistId
        .flatMapLatest { id ->
            if (id != null) playlistRepository.getPlaylistTracks(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _playlist = MutableStateFlow<PlaylistEntity?>(null)
    val playlist: StateFlow<PlaylistEntity?> = _playlist.asStateFlow()

    fun loadPlaylist(playlistId: Long) {
        _playlistId.value = playlistId
        viewModelScope.launch {
            _playlist.value = playlistRepository.getPlaylistById(playlistId)
        }
    }

    fun removeTrack(trackId: Long) {
        val pid = _playlistId.value ?: return
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist(pid, trackId)
        }
    }
}
