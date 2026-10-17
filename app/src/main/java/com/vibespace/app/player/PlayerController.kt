package com.vibespace.app.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.mediastore.MediaStoreScanner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Abstraction over the Media3 MediaController.
 * Provides a clean API for the UI layer to control playback
 * without directly dealing with MediaController/MediaSession.
 */
@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    // --- Observable State ---

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentTrack = MutableStateFlow<TrackEntity?>(null)
    val currentTrack: StateFlow<TrackEntity?> = _currentTrack.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _position = MutableStateFlow(0L)
    val position: StateFlow<Long> = _position.asStateFlow()

    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private val _hasNext = MutableStateFlow(false)
    val hasNext: StateFlow<Boolean> = _hasNext.asStateFlow()

    private val _hasPrevious = MutableStateFlow(false)
    val hasPrevious: StateFlow<Boolean> = _hasPrevious.asStateFlow()

    /**
     * Connect to the PlaybackService's MediaSession.
     * Should be called when the activity starts.
     */
    fun connect() {
        if (controllerFuture != null) return

        val sessionToken = SessionToken(
            context,
            ComponentName(context, PlaybackService::class.java)
        )

        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            mediaController = controllerFuture?.let {
                if (it.isDone && !it.isCancelled) it.get() else null
            }
            mediaController?.addListener(playerListener)
            updateState()
        }, MoreExecutors.directExecutor())
    }

    /**
     * Disconnect from the MediaSession.
     * Should be called when the activity stops.
     */
    fun disconnect() {
        mediaController?.removeListener(playerListener)
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null
        mediaController = null
    }

    // --- Playback Controls ---

    fun play() {
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun togglePlayPause() {
        val controller = mediaController ?: return
        if (controller.isPlaying) controller.pause() else controller.play()
    }

    fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
    }

    fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    fun skipToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    fun toggleShuffle() {
        val controller = mediaController ?: return
        controller.shuffleModeEnabled = !controller.shuffleModeEnabled
    }

    fun cycleRepeatMode() {
        val controller = mediaController ?: return
        controller.repeatMode = when (controller.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
    }

    /**
     * Play a single track immediately.
     */
    fun playTrack(track: TrackEntity) {
        val controller = mediaController ?: return
        val mediaItem = buildMediaItem(track)
        controller.setMediaItem(mediaItem)
        controller.prepare()
        controller.play()
        _currentTrack.value = track
    }

    /**
     * Play a list of tracks starting from a specific index.
     */
    fun playTracks(tracks: List<TrackEntity>, startIndex: Int = 0) {
        val controller = mediaController ?: return
        val mediaItems = tracks.map { buildMediaItem(it) }
        controller.setMediaItems(mediaItems, startIndex, 0L)
        controller.prepare()
        controller.play()
        if (startIndex in tracks.indices) {
            _currentTrack.value = tracks[startIndex]
        }
    }

    /**
     * Add a track to the end of the playback queue.
     */
    fun addToQueue(track: TrackEntity) {
        val controller = mediaController ?: return
        controller.addMediaItem(buildMediaItem(track))
    }

    /** Get the current playback position in milliseconds. */
    fun getCurrentPosition(): Long {
        return mediaController?.currentPosition ?: 0L
    }

    // --- Internal ---

    private fun buildMediaItem(track: TrackEntity): MediaItem {
        return MediaItem.Builder()
            .setUri(Uri.parse(track.uri))
            .setMediaId(track.id.toString())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(track.title)
                    .setArtist(track.artist)
                    .setAlbumTitle(track.album)
                    .setArtworkUri(MediaStoreScanner.getAlbumArtUri(track.albumId))
                    .build()
            )
            .build()
    }

    private fun updateState() {
        val controller = mediaController ?: return
        _isPlaying.value = controller.isPlaying
        _duration.value = controller.duration.coerceAtLeast(0L)
        _position.value = controller.currentPosition.coerceAtLeast(0L)
        _shuffleEnabled.value = controller.shuffleModeEnabled
        _repeatMode.value = controller.repeatMode
        _hasNext.value = controller.hasNextMediaItem()
        _hasPrevious.value = controller.hasPreviousMediaItem()
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateState()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            updateState()
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _shuffleEnabled.value = shuffleModeEnabled
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            _repeatMode.value = repeatMode
        }
    }
}
