package com.vibespace.app.data.repository

import com.vibespace.app.data.db.dao.PlaybackStateDao
import com.vibespace.app.data.db.dao.QueueDao
import com.vibespace.app.data.db.entity.PlaybackStateEntity
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for queue and playback state management.
 * Handles persisting the queue and playback position across app restarts.
 */
@Singleton
class QueueRepository @Inject constructor(
    private val queueDao: QueueDao,
    private val playbackStateDao: PlaybackStateDao
) {
    /** Observable list of tracks in the queue. */
    val queueTracks: Flow<List<TrackEntity>> = queueDao.getQueueTracks()

    /** Replace the entire queue and set the current track. */
    suspend fun replaceQueue(trackIds: List<Long>, currentTrackId: Long? = null) {
        queueDao.replaceQueue(trackIds, currentTrackId)
    }

    /** Add a track to the end of the queue. */
    suspend fun addToQueue(trackId: Long) {
        queueDao.addToQueue(trackId)
    }

    /** Remove a track from the queue. */
    suspend fun removeFromQueue(trackId: Long) {
        queueDao.removeFromQueue(trackId)
    }

    /** Clear the queue. */
    suspend fun clearQueue() {
        queueDao.clearQueue()
    }

    /** Set the currently playing track. */
    suspend fun setCurrentTrack(trackId: Long) {
        queueDao.setCurrentTrack(trackId)
    }

    // --- Playback State ---

    /** Get the saved playback state. */
    suspend fun getPlaybackState(): PlaybackStateEntity? {
        return playbackStateDao.getPlaybackState()
    }

    /** Save playback state. */
    suspend fun savePlaybackState(state: PlaybackStateEntity) {
        playbackStateDao.savePlaybackState(state)
    }

    /** Update just the playback position (called periodically). */
    suspend fun updatePosition(positionMs: Long) {
        playbackStateDao.updatePosition(positionMs)
    }

    /** Update shuffle mode. */
    suspend fun updateShuffle(enabled: Boolean) {
        playbackStateDao.updateShuffle(enabled)
    }

    /** Update repeat mode. */
    suspend fun updateRepeatMode(mode: Int) {
        playbackStateDao.updateRepeatMode(mode)
    }
}
