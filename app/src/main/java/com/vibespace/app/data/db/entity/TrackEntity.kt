package com.vibespace.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Cached representation of an audio track from the device's MediaStore.
 * We cache tracks in Room for fast queries, search, and offline access
 * to metadata without repeatedly querying the ContentResolver.
 */
@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,             // MediaStore audio ID
    val title: String,
    val artist: String,
    val album: String,
    val albumId: Long,
    val duration: Long,       // Duration in milliseconds
    val uri: String,          // content:// URI string
    val dateAdded: Long,      // Epoch seconds when added to device
    val trackNumber: Int = 0, // Track number within album
    val year: Int = 0,
    val isUploaded: Boolean = false // True if uploaded/imported directly by user
)
