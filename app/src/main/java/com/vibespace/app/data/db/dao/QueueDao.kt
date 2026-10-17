package com.vibespace.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vibespace.app.data.db.entity.QueueTrackEntity
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing the playback queue.
 * The queue is persisted to survive app restarts.
 */
@Dao
interface QueueDao {

    /** Get all queue entries ordered by position. */
    @Query("SELECT * FROM queue_tracks ORDER BY position ASC")
    fun getQueueEntries(): Flow<List<QueueTrackEntity>>

    /** Get all tracks in the queue with full track info, ordered by position. */
    @Query("""
        SELECT t.* FROM tracks t
        INNER JOIN queue_tracks qt ON t.id = qt.trackId
        ORDER BY qt.position ASC
    """)
    fun getQueueTracks(): Flow<List<TrackEntity>>

    /** Get the currently playing track's queue entry. */
    @Query("SELECT * FROM queue_tracks WHERE isCurrent = 1 LIMIT 1")
    suspend fun getCurrentQueueEntry(): QueueTrackEntity?

    /** Set the current track in the queue. */
    @Transaction
    suspend fun setCurrentTrack(trackId: Long) {
        clearCurrentFlag()
        markAsCurrent(trackId)
    }

    /** Add a track to the end of the queue. */
    @Transaction
    suspend fun addToQueue(trackId: Long) {
        val maxPosition = getMaxPosition() ?: -1
        insertQueueTrack(QueueTrackEntity(trackId = trackId, position = maxPosition + 1))
    }

    /** Replace the entire queue with a new list of track IDs. */
    @Transaction
    suspend fun replaceQueue(trackIds: List<Long>, currentTrackId: Long? = null) {
        clearQueue()
        trackIds.forEachIndexed { index, trackId ->
            insertQueueTrack(
                QueueTrackEntity(
                    trackId = trackId,
                    position = index,
                    isCurrent = trackId == currentTrackId
                )
            )
        }
    }

    /** Remove a specific track from the queue. */
    @Query("DELETE FROM queue_tracks WHERE trackId = :trackId")
    suspend fun removeFromQueue(trackId: Long)

    /** Clear the entire queue. */
    @Query("DELETE FROM queue_tracks")
    suspend fun clearQueue()

    // --- Internal helpers ---

    @Query("SELECT MAX(position) FROM queue_tracks")
    suspend fun getMaxPosition(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueueTrack(entry: QueueTrackEntity)

    @Query("UPDATE queue_tracks SET isCurrent = 0")
    suspend fun clearCurrentFlag()

    @Query("UPDATE queue_tracks SET isCurrent = 1 WHERE trackId = :trackId")
    suspend fun markAsCurrent(trackId: Long)
}
