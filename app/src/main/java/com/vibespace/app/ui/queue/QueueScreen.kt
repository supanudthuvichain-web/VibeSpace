package com.vibespace.app.ui.queue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.ui.components.TrackListItem
import com.vibespace.app.ui.theme.VibeSpaceTheme

@Composable
fun QueueScreen(
    viewModel: QueueViewModel,
    currentTrackId: Long?,
    onTrackClick: (TrackEntity, List<TrackEntity>) -> Unit,
    modifier: Modifier = Modifier
) {
    val queueTracks by viewModel.queueTracks.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Playing Queue",
                    style = VibeSpaceTheme.typography.headlineLarge,
                    color = VibeSpaceTheme.colors.onSurface
                )
                Text(
                    text = "${queueTracks.size} tracks",
                    style = VibeSpaceTheme.typography.bodyMedium,
                    color = VibeSpaceTheme.colors.textSecondary
                )
            }

            if (queueTracks.isNotEmpty()) {
                IconButton(onClick = viewModel::clearQueue) {
                    Icon(
                        imageVector = Icons.Default.ClearAll,
                        contentDescription = "Clear Queue",
                        tint = VibeSpaceTheme.colors.onSurface
                    )
                }
            }
        }

        if (queueTracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Queue is empty. Play a song or album to populate the queue.",
                    style = VibeSpaceTheme.typography.bodyLarge,
                    color = VibeSpaceTheme.colors.textSecondary
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(queueTracks, key = { "queue_${it.id}" }) { track ->
                    TrackListItem(
                        track = track,
                        isPlaying = track.id == currentTrackId,
                        onClick = { onTrackClick(track, queueTracks) },
                        onMoreClick = { viewModel.removeFromQueue(track.id) }
                    )
                }
            }
        }
    }
}
