package com.vibespace.app.ui

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibespace.app.data.db.entity.PlaylistEntity
import com.vibespace.app.data.db.entity.TrackEntity
import com.vibespace.app.ui.components.MiniPlayer
import com.vibespace.app.ui.home.HomeScreen
import com.vibespace.app.ui.home.HomeViewModel
import com.vibespace.app.ui.library.LibraryScreen
import com.vibespace.app.ui.library.LibraryViewModel
import com.vibespace.app.ui.nowplaying.NowPlayingContent
import com.vibespace.app.ui.player.PlayerViewModel
import com.vibespace.app.ui.playlist.PlaylistDetailScreen
import com.vibespace.app.ui.playlist.PlaylistDetailViewModel
import com.vibespace.app.ui.queue.QueueScreen
import com.vibespace.app.ui.queue.QueueViewModel
import com.vibespace.app.ui.search.SearchScreen
import com.vibespace.app.ui.search.SearchViewModel
import com.vibespace.app.ui.theme.VibeSpaceTheme
import com.vibespace.app.util.PermissionHelper

sealed class Screen(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("Home", Icons.Default.Home)
    object Library : Screen("Library", Icons.Default.LibraryMusic)
    object Search : Screen("Search", Icons.Default.Search)
    object Queue : Screen("Queue", Icons.Default.QueueMusic)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibeSpaceMainScreen(
    playerViewModel: PlayerViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    libraryViewModel: LibraryViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    queueViewModel: QueueViewModel = hiltViewModel(),
    playlistDetailViewModel: PlaylistDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedScreenIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedPlaylist by remember { mutableStateOf<PlaylistEntity?>(null) }
    var isNowPlayingOpen by remember { mutableStateOf(false) }

    val currentTrack by playerViewModel.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by playerViewModel.isPlaying.collectAsStateWithLifecycle()
    val currentPosition by playerViewModel.currentPosition.collectAsStateWithLifecycle()
    val duration by playerViewModel.duration.collectAsStateWithLifecycle()
    val shuffleEnabled by playerViewModel.shuffleEnabled.collectAsStateWithLifecycle()
    val repeatMode by playerViewModel.repeatMode.collectAsStateWithLifecycle()

    // Storage Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            homeViewModel.scanMusic()
        }
    }

    LaunchedEffect(Unit) {
        if (!PermissionHelper.hasAudioPermission(context)) {
            permissionLauncher.launch(PermissionHelper.audioPermission)
        }
    }

    val screens = listOf(
        Screen.Home,
        Screen.Library,
        Screen.Search,
        Screen.Queue
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = VibeSpaceTheme.colors.background,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                // Persistent Mini-Player when a track is active
                currentTrack?.let { track ->
                    val progress = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
                    MiniPlayer(
                        track = track,
                        isPlaying = isPlaying,
                        progress = progress,
                        onPlayPauseClick = playerViewModel::togglePlayPause,
                        onSkipNextClick = playerViewModel::skipToNext,
                        onClick = { isNowPlayingOpen = true }
                    )
                }

                // 4-Tab Bottom Navigation Bar
                NavigationBar(
                    containerColor = VibeSpaceTheme.colors.background,
                    tonalElevation = androidx.compose.ui.unit.Dp(0f)
                ) {
                    screens.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            selected = selectedScreenIndex == index && selectedPlaylist == null,
                            onClick = {
                                selectedPlaylist = null
                                selectedScreenIndex = index
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VibeSpaceTheme.colors.primary,
                                selectedTextColor = VibeSpaceTheme.colors.primary,
                                unselectedIconColor = VibeSpaceTheme.colors.textSecondary,
                                unselectedTextColor = VibeSpaceTheme.colors.textSecondary,
                                indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedPlaylist != null) {
                PlaylistDetailScreen(
                    playlistId = selectedPlaylist!!.id,
                    viewModel = playlistDetailViewModel,
                    currentTrackId = currentTrack?.id,
                    onBackClick = { selectedPlaylist = null },
                    onTrackClick = { track, list ->
                        playerViewModel.playTrack(track, list)
                    }
                )
            } else {
                when (selectedScreenIndex) {
                    0 -> HomeScreen(
                        viewModel = homeViewModel,
                        currentTrackId = currentTrack?.id,
                        onTrackClick = { track, list ->
                            playerViewModel.playTrack(track, list)
                        }
                    )
                    1 -> LibraryScreen(
                        viewModel = libraryViewModel,
                        currentTrackId = currentTrack?.id,
                        onTrackClick = { track, list ->
                            playerViewModel.playTrack(track, list)
                        },
                        onPlaylistClick = { playlist ->
                            selectedPlaylist = playlist
                        }
                    )
                    2 -> SearchScreen(
                        viewModel = searchViewModel,
                        currentTrackId = currentTrack?.id,
                        onTrackClick = { track, list ->
                            playerViewModel.playTrack(track, list)
                        }
                    )
                    3 -> QueueScreen(
                        viewModel = queueViewModel,
                        currentTrackId = currentTrack?.id,
                        onTrackClick = { track, list ->
                            playerViewModel.playTrack(track, list)
                        }
                    )
                }
            }
        }
    }

    // Now Playing Modal Bottom Sheet
    if (isNowPlayingOpen && currentTrack != null) {
        ModalBottomSheet(
            onDismissRequest = { isNowPlayingOpen = false },
            sheetState = sheetState,
            containerColor = VibeSpaceTheme.colors.background,
            dragHandle = null
        ) {
            NowPlayingContent(
                track = currentTrack!!,
                isPlaying = isPlaying,
                currentPosition = currentPosition,
                duration = duration,
                shuffleEnabled = shuffleEnabled,
                repeatMode = repeatMode,
                onPlayPauseClick = playerViewModel::togglePlayPause,
                onSkipNextClick = playerViewModel::skipToNext,
                onSkipPrevClick = playerViewModel::skipToPrevious,
                onSeekTo = playerViewModel::seekTo,
                onToggleShuffle = playerViewModel::toggleShuffle,
                onCycleRepeat = playerViewModel::cycleRepeatMode,
                onCollapse = { isNowPlayingOpen = false }
            )
        }
    }
}
