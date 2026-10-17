package com.vibespace.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vibespace.app.data.db.entity.RecentlyPlayedEntity
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for tracking recently played tracks.
 */
@Dao
interface RecentlyPlayedDao {

    /** Record a track as recently played. */
    @Insert
    suspend fun recordPlay(entry: RecentlyPlayedEntity)

    /**
     * Get recently played tracks (distinct, most recent first).
     * Limited to 50 entries to keep the list manageable.
     */
    @Query("""
        SELECT t.* FROM tracks t
        INNER JOIN (
            SELECT trackId, MAX(playedAt) as lastPlayed
            FROM recently_played
            GROUP BY trackId
        ) rp ON t.id = rp.trackId
        ORDER BY rp.lastPlayed DESC
        LIMIT 50
    """)
    fun getRecentlyPlayed(): Flow<List<TrackEntity>>

    /** Clear all play history. */
    @Query("DELETE FROM recently_played")
    suspend fun clearHistory()

    /** Keep only the latest 200 entries to prevent unbounded growth. */
    @Query("""
        DELETE FROM recently_played WHERE id NOT IN (
            SELECT id FROM recently_played ORDER BY playedAt DESC LIMIT 200
        )
    """)
    suspend fun pruneOldEntries()
}
