package com.vibespace.app.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun SearchScreen(
    viewModel: SearchViewModel,
    currentTrackId: Long?,
    onTrackClick: (TrackEntity, List<TrackEntity>) -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Search",
            style = VibeSpaceTheme.typography.headlineLarge,
            color = VibeSpaceTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        // Search Input Bar
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text(
                    text = "Songs, artists, or albums...",
                    color = VibeSpaceTheme.colors.textSecondary
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = VibeSpaceTheme.colors.textSecondary
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = viewModel::clearQuery) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = VibeSpaceTheme.colors.textSecondary
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VibeSpaceTheme.colors.surfaceElevated,
                unfocusedContainerColor = VibeSpaceTheme.colors.surfaceElevated,
                focusedBorderColor = VibeSpaceTheme.colors.primary,
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedTextColor = VibeSpaceTheme.colors.onSurface,
                unfocusedTextColor = VibeSpaceTheme.colors.onSurface
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (query.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Search for your favorite songs, artists, or albums.",
                    style = VibeSpaceTheme.typography.bodyLarge,
                    color = VibeSpaceTheme.colors.textSecondary
                )
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found for \"$query\"",
                    style = VibeSpaceTheme.typography.bodyLarge,
                    color = VibeSpaceTheme.colors.textSecondary
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchResults, key = { it.id }) { track ->
                    TrackListItem(
                        track = track,
                        isPlaying = track.id == currentTrackId,
                        onClick = { onTrackClick(track, searchResults) }
                    )
                }
            }
        }
    }
}
