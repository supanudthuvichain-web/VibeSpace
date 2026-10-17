package com.vibespace.app.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val queueRepository: QueueRepository
) : ViewModel() {

    val queueTracks: StateFlow<List<TrackEntity>> = queueRepository.queueTracks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFromQueue(trackId: Long) {
        viewModelScope.launch {
            queueRepository.removeFromQueue(trackId)
        }
    }

    fun clearQueue() {
        viewModelScope.launch {
            queueRepository.clearQueue()
        }
    }
}
