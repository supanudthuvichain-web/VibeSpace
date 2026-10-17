package com.vibespace.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.vibespace.app.ui.VibeSpaceMainScreen
import com.vibespace.app.ui.theme.VibeSpaceTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point activity for VibeSpace.
 * Uses edge-to-edge rendering and hosts the Compose UI tree.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VibeSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VibeSpaceTheme.colors.background
                ) {
                    VibeSpaceMainScreen()
                }
            }
        }
    }
}
