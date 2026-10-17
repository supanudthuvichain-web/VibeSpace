package com.vibespace.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vibespace.app.data.db.entity.PlaybackStateEntity

/**
 * DAO for the singleton playback state.
 * Persists current track, position, shuffle, and repeat mode.
 */
@Dao
interface PlaybackStateDao {

    /** Get the saved playback state (or null if none saved). */
    @Query("SELECT * FROM playback_state WHERE id = 1")
    suspend fun getPlaybackState(): PlaybackStateEntity?

    /** Save or update playback state. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlaybackState(state: PlaybackStateEntity)

    /** Update just the playback position. */
    @Query("UPDATE playback_state SET positionMs = :positionMs WHERE id = 1")
    suspend fun updatePosition(positionMs: Long)

    /** Update the current track and reset position. */
    @Query("UPDATE playback_state SET currentTrackId = :trackId, positionMs = 0 WHERE id = 1")
    suspend fun updateCurrentTrack(trackId: Long)

    /** Update shuffle mode. */
    @Query("UPDATE playback_state SET shuffleEnabled = :enabled WHERE id = 1")
    suspend fun updateShuffle(enabled: Boolean)

    /** Update repeat mode. */
    @Query("UPDATE playback_state SET repeatMode = :mode WHERE id = 1")
    suspend fun updateRepeatMode(mode: Int)
}
