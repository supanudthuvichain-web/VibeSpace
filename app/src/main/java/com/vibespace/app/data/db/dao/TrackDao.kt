package com.vibespace.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vibespace.app.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for cached audio tracks.
 */
@Dao
interface TrackDao {

    /** Insert or update tracks (used during MediaStore scan). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<TrackEntity>)

    /** Insert or update a single track (used when uploading/importing a song). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    /** Get all tracks sorted by title. */
    @Query("SELECT * FROM tracks ORDER BY title ASC")
    fun getAllTracks(): Flow<List<TrackEntity>>

    /** Get all uploaded tracks sorted by dateAdded descending. */
    @Query("SELECT * FROM tracks WHERE isUploaded = 1 ORDER BY dateAdded DESC")
    fun getUploadedTracks(): Flow<List<TrackEntity>>

    /** Get tracks by album. */
    @Query("SELECT * FROM tracks WHERE albumId = :albumId ORDER BY trackNumber ASC, title ASC")
    fun getTracksByAlbum(albumId: Long): Flow<List<TrackEntity>>

    /** Get tracks by artist. */
    @Query("SELECT * FROM tracks WHERE artist = :artist ORDER BY album ASC, trackNumber ASC")
    fun getTracksByArtist(artist: String): Flow<List<TrackEntity>>

    /** Get a single track by ID. */
    @Query("SELECT * FROM tracks WHERE id = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?

    /** Get multiple tracks by IDs. */
    @Query("SELECT * FROM tracks WHERE id IN (:trackIds)")
    suspend fun getTracksByIds(trackIds: List<Long>): List<TrackEntity>

    /** Search tracks by title, artist, or album name. */
    @Query("""
        SELECT * FROM tracks 
        WHERE title LIKE '%' || :query || '%' 
           OR artist LIKE '%' || :query || '%' 
           OR album LIKE '%' || :query || '%'
        ORDER BY title ASC
        LIMIT 50
    """)
    fun searchTracks(query: String): Flow<List<TrackEntity>>

    /** Get distinct albums as (albumId, album, artist) tuples. */
    @Query("""
        SELECT DISTINCT albumId, album, artist 
        FROM tracks 
        ORDER BY album ASC
    """)
    fun getAlbums(): Flow<List<AlbumInfo>>

    /** Get distinct artist names. */
    @Query("SELECT DISTINCT artist FROM tracks ORDER BY artist ASC")
    fun getArtists(): Flow<List<String>>

    /** Delete cached MediaStore tracks only, preserving uploaded tracks. */
    @Query("DELETE FROM tracks WHERE isUploaded = 0")
    suspend fun deleteMediaStoreTracks()

    /** Delete a single track by ID. */
    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    /** Delete all cached tracks. */
    @Query("DELETE FROM tracks")
    suspend fun deleteAll()

    /** Get total track count. */
    @Query("SELECT COUNT(*) FROM tracks")
    suspend fun getTrackCount(): Int
}

/** Lightweight projection for album listing. */
data class AlbumInfo(
    val albumId: Long,
    val album: String,
    val artist: String
)
