package com.vibespace.app.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import com.vibespace.app.data.db.dao.AlbumInfo
import com.vibespace.app.data.db.dao.TrackDao
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.mediastore.MediaStoreScanner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for music library operations.
 * Combines MediaStore scanning with Room-cached track data and uploaded audio files.
 */
@Singleton
class MusicRepository @Inject constructor(
    private val trackDao: TrackDao,
    private val mediaStoreScanner: MediaStoreScanner,
    @ApplicationContext private val context: Context
) {
    /** Observable list of all tracks. */
    val allTracks: Flow<List<TrackEntity>> = trackDao.getAllTracks()

    /** Observable list of user-uploaded tracks. */
    val uploadedTracks: Flow<List<TrackEntity>> = trackDao.getUploadedTracks()

    /** Observable list of distinct albums. */
    val allAlbums: Flow<List<AlbumInfo>> = trackDao.getAlbums()

    /** Observable list of distinct artists. */
    val allArtists: Flow<List<String>> = trackDao.getArtists()

    /**
     * Scan the device for music and cache results in Room.
     * Preserves user-uploaded tracks.
     */
    suspend fun scanAndCacheMusic() {
        val tracks = mediaStoreScanner.scanDeviceMusic()
        trackDao.deleteMediaStoreTracks()
        trackDao.insertAll(tracks)
    }

    /**
     * Imports an audio file chosen by the user (upload song).
     * Copies the file to the app's internal storage, extracts metadata,
     * and inserts the track entity into Room.
     */
    suspend fun importAudio(uri: Uri): TrackEntity? = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver

            // Determine file name
            var fileName = "track_${System.currentTimeMillis()}.mp3"
            try {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1 && cursor.moveToFirst()) {
                        val name = cursor.getString(nameIndex)
                        if (!name.isNullOrBlank()) {
                            fileName = name
                        }
                    }
                }
            } catch (e: Exception) {
                uri.lastPathSegment?.let { segment ->
                    val cleanSegment = segment.substringAfterLast("/")
                    if (cleanSegment.isNotBlank()) fileName = cleanSegment
                }
            }

            // Copy file to internal storage
            val uploadDir = File(context.filesDir, "uploaded_music")
            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }
            val destinationFile = File(uploadDir, "${System.currentTimeMillis()}_$fileName")
            contentResolver.openInputStream(uri)?.use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return@withContext null

            // Extract metadata via MediaMetadataRetriever
            val retriever = MediaMetadataRetriever()
            var title: String? = null
            var artist: String? = null
            var album: String? = null
            var duration = 0L
            var year = 0

            try {
                retriever.setDataSource(destinationFile.absolutePath)
                title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.toIntOrNull() ?: 0
            } catch (e: Exception) {
                // Fall back gracefully
            } finally {
                try {
                    retriever.release()
                } catch (e: Exception) {}
            }

            val fallbackTitle = fileName.substringBeforeLast(".")
            val finalTitle = if (!title.isNullOrBlank()) title.trim() else fallbackTitle
            val finalArtist = if (!artist.isNullOrBlank()) artist.trim() else "Uploaded Artist"
            val finalAlbum = if (!album.isNullOrBlank()) album.trim() else "Uploaded Music"

            val track = TrackEntity(
                id = System.currentTimeMillis() + kotlin.random.Random.nextLong(1, 1000),
                title = finalTitle,
                artist = finalArtist,
                album = finalAlbum,
                albumId = -1L,
                duration = duration,
                uri = Uri.fromFile(destinationFile).toString(),
                dateAdded = System.currentTimeMillis() / 1000,
                trackNumber = 0,
                year = year,
                isUploaded = true
            )

            trackDao.insertTrack(track)
            track
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Imports multiple audio files chosen by the user.
     */
    suspend fun importAudioFiles(uris: List<Uri>): List<TrackEntity> = withContext(Dispatchers.IO) {
        val imported = mutableListOf<TrackEntity>()
        for (uri in uris) {
            importAudio(uri)?.let { imported.add(it) }
        }
        imported
    }

    /** Delete a track from database and storage if it was an uploaded file. */
    suspend fun deleteTrack(track: TrackEntity) = withContext(Dispatchers.IO) {
        if (track.isUploaded && track.uri.startsWith("file://")) {
            try {
                val file = File(Uri.parse(track.uri).path ?: "")
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        trackDao.deleteTrackById(track.id)
    }

    /** Get a single track by ID. */
    suspend fun getTrackById(trackId: Long): TrackEntity? {
        return trackDao.getTrackById(trackId)
    }

    /** Get multiple tracks by IDs. */
    suspend fun getTracksByIds(trackIds: List<Long>): List<TrackEntity> {
        return trackDao.getTracksByIds(trackIds)
    }

    /** Get tracks for a specific album. */
    fun getTracksByAlbum(albumId: Long): Flow<List<TrackEntity>> {
        return trackDao.getTracksByAlbum(albumId)
    }

    /** Get tracks for a specific artist. */
    fun getTracksByArtist(artist: String): Flow<List<TrackEntity>> {
        return trackDao.getTracksByArtist(artist)
    }

    /** Search tracks by query string. */
    fun searchTracks(query: String): Flow<List<TrackEntity>> {
        return trackDao.searchTracks(query)
    }

    /** Check if the music cache is empty (needs scanning). */
    suspend fun isCacheEmpty(): Boolean {
        return trackDao.getTrackCount() == 0
    }
}
