package com.vibespace.app.data.repository

import com.vibespace.app.data.db.dao.PlaylistDao
import com.vibespace.app.data.db.entity.PlaylistEntity
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for playlist operations.
 */
@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {
    /** Observable list of all playlists. */
    val allPlaylists: Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists()

    /** Create a new playlist and return its ID. */
    suspend fun createPlaylist(name: String): Long {
        return playlistDao.createPlaylist(PlaylistEntity(name = name))
    }

    /** Get a playlist by ID. */
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return playlistDao.getPlaylistById(playlistId)
    }

    /** Rename a playlist. */
    suspend fun renamePlaylist(playlistId: Long, name: String) {
        playlistDao.renamePlaylist(playlistId, name)
    }

    /** Delete a playlist. */
    suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.deletePlaylist(playlistId)
    }

    /** Get tracks in a playlist as a reactive Flow. */
    fun getPlaylistTracks(playlistId: Long): Flow<List<TrackEntity>> {
        return playlistDao.getPlaylistTracks(playlistId)
    }

    /** Add a track to a playlist. */
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        playlistDao.addTrackToPlaylist(playlistId, trackId)
    }

    /** Remove a track from a playlist. */
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        playlistDao.removeTrackFromPlaylist(playlistId, trackId)
    }

    /** Get the number of tracks in a playlist. */
    suspend fun getPlaylistTrackCount(playlistId: Long): Int {
        return playlistDao.getPlaylistTrackCount(playlistId)
    }
}
