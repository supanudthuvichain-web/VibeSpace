package com.vibespace.app.data.repository

import com.vibespace.app.data.db.dao.RecentlyPlayedDao
import com.vibespace.app.data.db.entity.RecentlyPlayedEntity
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for recently played track history.
 */
@Singleton
class RecentlyPlayedRepository @Inject constructor(
    private val recentlyPlayedDao: RecentlyPlayedDao
) {
    /** Observable list of recently played tracks (most recent first). */
    val recentlyPlayed: Flow<List<TrackEntity>> = recentlyPlayedDao.getRecentlyPlayed()

    /** Record a track as played. Also prunes old entries. */
    suspend fun recordTrackPlayed(trackId: Long) {
        recentlyPlayedDao.recordPlay(RecentlyPlayedEntity(trackId = trackId))
        recentlyPlayedDao.pruneOldEntries()
    }

    /** Clear all play history. */
    suspend fun clearHistory() {
        recentlyPlayedDao.clearHistory()
    }
}
