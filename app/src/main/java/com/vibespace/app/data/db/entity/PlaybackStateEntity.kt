package com.vibespace.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Singleton entity to persist playback state (position, shuffle, repeat)
 * across app restarts. Always uses id=1.
 */
@Entity(tableName = "playback_state")
data class PlaybackStateEntity(
    @PrimaryKey
    val id: Int = 1,
    val currentTrackId: Long? = null,
    val positionMs: Long = 0L,
    val shuffleEnabled: Boolean = false,
    val repeatMode: Int = 0 // 0=off, 1=all, 2=one
)
