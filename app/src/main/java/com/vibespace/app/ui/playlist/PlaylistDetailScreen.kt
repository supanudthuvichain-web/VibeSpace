package com.vibespace.app.ui.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.ui.components.TrackListItem
import com.vibespace.app.ui.theme.VibeSpaceTheme

@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    viewModel: PlaylistDetailViewModel,
    currentTrackId: Long?,
    onBackClick: () -> Unit,
    onTrackClick: (TrackEntity, List<TrackEntity>) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(playlistId) {
        viewModel.loadPlaylist(playlistId)
    }

    val playlist by viewModel.playlist.collectAsStateWithLifecycle()
    val tracks by viewModel.playlistTracks.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = VibeSpaceTheme.colors.onSurface
                )
            }
            Text(
                text = playlist?.name ?: "Playlist",
                style = VibeSpaceTheme.typography.titleLarge,
                color = VibeSpaceTheme.colors.onSurface
            )
        }

        // Header Card with Play Button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            color = VibeSpaceTheme.colors.surfaceElevated,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VibeSpaceTheme.colors.surfaceVariant,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlaylistPlay,
                        contentDescription = null,
                        tint = VibeSpaceTheme.colors.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = playlist?.name ?: "",
                        style = VibeSpaceTheme.typography.headlineSmall,
                        color = VibeSpaceTheme.colors.onSurface
                    )
                    Text(
                        text = "${tracks.size} songs",
                        style = VibeSpaceTheme.typography.bodyMedium,
                        color = VibeSpaceTheme.colors.textSecondary
                    )
                }

                if (tracks.isNotEmpty()) {
                    Button(
                        onClick = { onTrackClick(tracks.first(), tracks) },
                        colors = ButtonDefaults.buttonColors(containerColor = VibeSpaceTheme.colors.primary),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play All")
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Play")
                    }
                }
            }
        }

        if (tracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "This playlist is empty. Add songs from your Library.",
                    style = VibeSpaceTheme.typography.bodyLarge,
                    color = VibeSpaceTheme.colors.textSecondary
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tracks, key = { it.id }) { track ->
                    TrackListItem(
                        track = track,
                        isPlaying = track.id == currentTrackId,
                        onClick = { onTrackClick(track, tracks) },
                        onMoreClick = { viewModel.removeTrack(track.id) }
                    )
                }
            }
        }
    }
}
