package com.vibespace.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vibespace.app.data.db.dao.PlaybackStateDao
import com.vibespace.app.data.db.dao.PlaylistDao
import com.vibespace.app.data.db.dao.QueueDao
import com.vibespace.app.data.db.dao.RecentlyPlayedDao
import com.vibespace.app.data.db.dao.TrackDao
import com.vibespace.app.data.db.entity.PlaybackStateEntity
import com.vibespace.app.data.db.entity.PlaylistEntity
import com.vibespace.app.data.db.entity.PlaylistTrackEntity
import com.vibespace.app.data.db.entity.QueueTrackEntity
import com.vibespace.app.data.db.entity.RecentlyPlayedEntity
import com.vibespace.app.data.db.entity.TrackEntity

/**
 * Main Room database for VibeSpace.
 * Stores cached tracks, playlists, queue state, playback state, and play history.
 */
@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class,
        QueueTrackEntity::class,
        PlaybackStateEntity::class,
        RecentlyPlayedEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VibeSpaceDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun queueDao(): QueueDao
    abstract fun playbackStateDao(): PlaybackStateDao
    abstract fun recentlyPlayedDao(): RecentlyPlayedDao
}
