package com.vibespace.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vibespace.app.data.db.entity.PlaylistEntity
import com.vibespace.app.data.db.entity.PlaylistTrackEntity
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for playlist CRUD and playlist-track junction operations.
 */
@Dao
interface PlaylistDao {

    /** Create a new playlist. Returns the auto-generated ID. */
    @Insert
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    /** Get all playlists sorted by most recently updated. */
    @Query("SELECT * FROM playlists ORDER BY updatedAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    /** Get a single playlist by ID. */
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    /** Rename a playlist. */
    @Query("UPDATE playlists SET name = :name, updatedAt = :updatedAt WHERE id = :playlistId")
    suspend fun renamePlaylist(playlistId: Long, name: String, updatedAt: Long = System.currentTimeMillis())

    /** Delete a playlist (cascade deletes junction rows). */
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    /** Add a track to a playlist at the end. */
    @Transaction
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        val maxPosition = getMaxPosition(playlistId) ?: -1
        insertPlaylistTrack(PlaylistTrackEntity(playlistId, trackId, maxPosition + 1))
        touchPlaylist(playlistId)
    }

    /** Remove a track from a playlist. */
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    /** Get all tracks in a playlist, ordered by position. */
    @Query("""
        SELECT t.* FROM tracks t
        INNER JOIN playlist_tracks pt ON t.id = pt.trackId
        WHERE pt.playlistId = :playlistId
        ORDER BY pt.position ASC
    """)
    fun getPlaylistTracks(playlistId: Long): Flow<List<TrackEntity>>

    /** Get the track count for a playlist. */
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getPlaylistTrackCount(playlistId: Long): Int

    // --- Internal helpers ---

    @Query("SELECT MAX(position) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getMaxPosition(playlistId: Long): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackEntity)

    @Query("UPDATE playlists SET updatedAt = :updatedAt WHERE id = :playlistId")
    suspend fun touchPlaylist(playlistId: Long, updatedAt: Long = System.currentTimeMillis())
}
