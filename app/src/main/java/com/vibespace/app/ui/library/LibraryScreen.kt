package com.vibespace.app.ui.library

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibespace.app.data.db.entity.PlaylistEntity
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.ui.components.AlbumCard
import com.vibespace.app.ui.components.TrackListItem
import com.vibespace.app.ui.theme.VibeSpaceTheme

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    currentTrackId: Long?,
    onTrackClick: (TrackEntity, List<TrackEntity>) -> Unit,
    onPlaylistClick: (PlaylistEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Songs", "Albums", "Artists", "Playlists")

    val tracks by viewModel.allTracks.collectAsStateWithLifecycle()
    val albums by viewModel.allAlbums.collectAsStateWithLifecycle()
    val artists by viewModel.allArtists.collectAsStateWithLifecycle()
    val playlists by viewModel.allPlaylists.collectAsStateWithLifecycle()

    var showCreatePlaylistDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Your Library",
                style = VibeSpaceTheme.typography.headlineLarge,
                color = VibeSpaceTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            // Sub-tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = VibeSpaceTheme.colors.primary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = VibeSpaceTheme.colors.primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = VibeSpaceTheme.typography.titleSmall,
                                color = if (selectedTabIndex == index) {
                                    VibeSpaceTheme.colors.primary
                                } else {
                                    VibeSpaceTheme.colors.textSecondary
                                }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedTabIndex) {
                // 0: Songs
                0 -> {
                    if (tracks.isEmpty()) {
                        EmptyLibraryState(message = "No songs found on your device.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 120.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(tracks, key = { it.id }) { track ->
                                TrackListItem(
                                    track = track,
                                    isPlaying = track.id == currentTrackId,
                                    onClick = { onTrackClick(track, tracks) }
                                )
                            }
                        }
                    }
                }
                // 1: Albums
                1 -> {
                    if (albums.isEmpty()) {
                        EmptyLibraryState(message = "No albums found.")
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(albums, key = { it.albumId }) { album ->
                                AlbumCard(
                                    album = album,
                                    onClick = {
                                        val albumTracks = tracks.filter { it.albumId == album.albumId }
                                        if (albumTracks.isNotEmpty()) {
                                            onTrackClick(albumTracks.first(), albumTracks)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                // 2: Artists
                2 -> {
                    if (artists.isEmpty()) {
                        EmptyLibraryState(message = "No artists found.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 120.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(artists, key = { it }) { artist ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val artistTracks = tracks.filter { it.artist == artist }
                                            if (artistTracks.isNotEmpty()) {
                                                onTrackClick(artistTracks.first(), artistTracks)
                                            }
                                        },
                                    color = androidx.compose.ui.graphics.Color.Transparent
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(24.dp),
                                            color = VibeSpaceTheme.colors.surfaceElevated,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = VibeSpaceTheme.colors.primary,
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }

                                        Column(modifier = Modifier.padding(start = 16.dp)) {
                                            Text(
                                                text = artist,
                                                style = VibeSpaceTheme.typography.titleMedium,
                                                color = VibeSpaceTheme.colors.onSurface
                                            )
                                            val trackCount = tracks.count { it.artist == artist }
                                            Text(
                                                text = "$trackCount songs",
                                                style = VibeSpaceTheme.typography.bodySmall,
                                                color = VibeSpaceTheme.colors.textSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // 3: Playlists
                3 -> {
                    if (playlists.isEmpty()) {
                        EmptyLibraryState(message = "No playlists created yet.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 120.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(playlists, key = { it.id }) { playlist ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onPlaylistClick(playlist) },
                                    color = androidx.compose.ui.graphics.Color.Transparent
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = VibeSpaceTheme.colors.surfaceElevated,
                                            modifier = Modifier.size(52.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlaylistPlay,
                                                contentDescription = null,
                                                tint = VibeSpaceTheme.colors.primary,
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            Text(
                                                text = playlist.name,
                                                style = VibeSpaceTheme.typography.titleMedium,
                                                color = VibeSpaceTheme.colors.onSurface,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "Playlist",
                                                style = VibeSpaceTheme.typography.bodySmall,
                                                color = VibeSpaceTheme.colors.textSecondary
                                            )
                                        }

                                        IconButton(onClick = { viewModel.deletePlaylist(playlist.id) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Playlist",
                                                tint = VibeSpaceTheme.colors.textTertiary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button on Playlists Tab to create a new playlist
        if (selectedTabIndex == 3) {
            FloatingActionButton(
                onClick = { showCreatePlaylistDialog = true },
                containerColor = VibeSpaceTheme.colors.primary,
                contentColor = VibeSpaceTheme.colors.onSurface,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 120.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Playlist")
            }
        }
    }

    // Create Playlist Dialog
    if (showCreatePlaylistDialog) {
        var playlistName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreatePlaylistDialog = false },
            title = {
                Text(
                    text = "New Playlist",
                    style = VibeSpaceTheme.typography.titleLarge,
                    color = VibeSpaceTheme.colors.onSurface
                )
            },
            text = {
                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = { Text("Playlist name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VibeSpaceTheme.colors.primary,
                        unfocusedBorderColor = VibeSpaceTheme.colors.outline,
                        focusedTextColor = VibeSpaceTheme.colors.onSurface,
                        unfocusedTextColor = VibeSpaceTheme.colors.onSurface
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (playlistName.isNotBlank()) {
                            viewModel.createPlaylist(playlistName)
                            showCreatePlaylistDialog = false
                        }
                    }
                ) {
                    Text("Create", color = VibeSpaceTheme.colors.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreatePlaylistDialog = false }) {
                    Text("Cancel", color = VibeSpaceTheme.colors.textSecondary)
                }
            },
            containerColor = VibeSpaceTheme.colors.surfaceElevated
        )
    }
}

@Composable
private fun EmptyLibraryState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = VibeSpaceTheme.typography.bodyLarge,
            color = VibeSpaceTheme.colors.textSecondary
        )
    }
}
