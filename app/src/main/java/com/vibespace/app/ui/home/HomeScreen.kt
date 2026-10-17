package com.vibespace.app.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.data.mediastore.MediaStoreScanner
import com.vibespace.app.ui.components.TrackListItem
import com.vibespace.app.ui.theme.VibeSpaceTheme
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    currentTrackId: Long?,
    onTrackClick: (TrackEntity, List<TrackEntity>) -> Unit,
    modifier: Modifier = Modifier
) {
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsStateWithLifecycle()
    val uploadedTracks by viewModel.uploadedTracks.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    val uploadMessage by viewModel.uploadMessage.collectAsStateWithLifecycle()

    // File picker launcher for audio files (.mp3, .wav, .flac, etc.)
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.uploadSongFiles(uris)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.scanMusicIfEmpty()
    }

    // Auto-dismiss upload status notification after a delay
    LaunchedEffect(uploadMessage) {
        if (uploadMessage != null) {
            delay(3500)
            viewModel.clearUploadMessage()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // App Bar / Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VibeSpace",
                        style = VibeSpaceTheme.typography.headlineLarge,
                        color = VibeSpaceTheme.colors.onSurface
                    )
                    Text(
                        text = "Your personal sound library",
                        style = VibeSpaceTheme.typography.bodyMedium,
                        color = VibeSpaceTheme.colors.textSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Upload Song File button
                    IconButton(
                        onClick = { audioPickerLauncher.launch(arrayOf("audio/*")) },
                        enabled = !isUploading
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = VibeSpaceTheme.colors.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Upload Song File",
                                tint = VibeSpaceTheme.colors.primary
                            )
                        }
                    }

                    // Scan Media button
                    IconButton(
                        onClick = { viewModel.scanMusic() },
                        enabled = !isScanning
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = VibeSpaceTheme.colors.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Scan Media",
                                tint = VibeSpaceTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Upload Status Banner
        item {
            AnimatedVisibility(
                visible = uploadMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uploadMessage?.let { msg ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        color = VibeSpaceTheme.colors.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, VibeSpaceTheme.colors.primary.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = VibeSpaceTheme.colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = msg,
                                style = VibeSpaceTheme.typography.bodyMedium,
                                color = VibeSpaceTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Quick Action Upload Song Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clickable(enabled = !isUploading) {
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    },
                color = VibeSpaceTheme.colors.surfaceElevated,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.dp,
                    VibeSpaceTheme.colors.primary.copy(alpha = 0.25f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = VibeSpaceTheme.colors.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = VibeSpaceTheme.colors.primary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    tint = VibeSpaceTheme.colors.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isUploading) "Uploading song..." else "Upload Song File",
                            style = VibeSpaceTheme.typography.titleMedium,
                            color = VibeSpaceTheme.colors.onSurface
                        )
                        Text(
                            text = "Add audio files (.mp3, .wav, .flac) to your app",
                            style = VibeSpaceTheme.typography.bodySmall,
                            color = VibeSpaceTheme.colors.textSecondary
                        )
                    }

                    Button(
                        onClick = { audioPickerLauncher.launch(arrayOf("audio/*")) },
                        enabled = !isUploading,
                        colors = ButtonDefaults.buttonColors(containerColor = VibeSpaceTheme.colors.primary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isUploading) "Adding..." else "Upload",
                            style = VibeSpaceTheme.typography.labelMedium,
                            color = VibeSpaceTheme.colors.onSurface
                        )
                    }
                }
            }
        }

        // Uploaded Songs Showcase Section
        if (uploadedTracks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Uploaded Songs",
                        style = VibeSpaceTheme.typography.headlineSmall,
                        color = VibeSpaceTheme.colors.onSurface
                    )
                    Text(
                        text = "${uploadedTracks.size} track${if (uploadedTracks.size > 1) "s" else ""}",
                        style = VibeSpaceTheme.typography.bodySmall,
                        color = VibeSpaceTheme.colors.primary
                    )
                }
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(uploadedTracks, key = { "uploaded_${it.id}" }) { track ->
                        RecentTrackCard(
                            track = track,
                            onClick = { onTrackClick(track, uploadedTracks) }
                        )
                    }
                }
            }
        }

        // Recently Played Section
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Recently Played",
                style = VibeSpaceTheme.typography.headlineSmall,
                color = VibeSpaceTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            if (recentlyPlayed.isEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    color = VibeSpaceTheme.colors.surfaceElevated,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = VibeSpaceTheme.colors.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tracks played yet",
                            style = VibeSpaceTheme.typography.titleMedium,
                            color = VibeSpaceTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Upload songs directly or scan your device to start listening.",
                            style = VibeSpaceTheme.typography.bodyMedium,
                            color = VibeSpaceTheme.colors.textSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { audioPickerLauncher.launch(arrayOf("audio/*")) },
                                colors = ButtonDefaults.buttonColors(containerColor = VibeSpaceTheme.colors.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload Song", color = VibeSpaceTheme.colors.onSurface)
                            }

                            OutlinedButton(
                                onClick = { viewModel.scanMusic() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = VibeSpaceTheme.colors.onSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Scan Device")
                            }
                        }
                    }
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(recentlyPlayed, key = { it.id }) { track ->
                        RecentTrackCard(
                            track = track,
                            onClick = { onTrackClick(track, recentlyPlayed) }
                        )
                    }
                }
            }
        }

        // Jump Back In section
        if (recentlyPlayed.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Jump Back In",
                    style = VibeSpaceTheme.typography.headlineSmall,
                    color = VibeSpaceTheme.colors.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            items(recentlyPlayed.take(8), key = { "recent_${it.id}" }) { track ->
                TrackListItem(
                    track = track,
                    isPlaying = track.id == currentTrackId,
                    onClick = { onTrackClick(track, recentlyPlayed) }
                )
            }
        }
    }
}

@Composable
private fun RecentTrackCard(
    track: TrackEntity,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = VibeSpaceTheme.colors.surfaceElevated,
                modifier = Modifier.matchParentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = VibeSpaceTheme.colors.textTertiary,
                    modifier = Modifier.padding(24.dp)
                )
            }

            AsyncImage(
                model = MediaStoreScanner.getAlbumArtUri(track.albumId),
                contentDescription = track.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = track.title,
            style = VibeSpaceTheme.typography.titleSmall,
            color = VibeSpaceTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = track.artist,
            style = VibeSpaceTheme.typography.bodySmall,
            color = VibeSpaceTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
