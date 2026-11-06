package com.spotifast.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotifast.android.ui.components.WinampMiniPlayer
import com.spotifast.android.ui.theme.SpotifastTheme
import com.spotifast.android.ui.theme.WinampMetalDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WinampMetalDark
                ) {
                    SpotifastApp()
                }
            }
        }
    }
}

@Composable
fun SpotifastApp() {
    var trackTitle by remember { mutableStateOf("Ready (librespot Rust backend)") }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.0f) }
    var spotifyUriInput by remember { mutableStateOf("spotify:track:4cOdK2wGLETKBW3PvgPWqT") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Signature Winamp Miniplayer widget
        WinampMiniPlayer(
            trackTitle = trackTitle,
            isPlaying = isPlaying,
            progress = progress,
            onPlayPause = {
                if (isPlaying) {
                    SpotifastNative.pauseTrack()
                    isPlaying = false
                } else {
                    SpotifastNative.resumeTrack()
                    isPlaying = true
                }
            },
            onPrevious = { /* Skip previous */ },
            onNext = { /* Skip next */ },
            onSeek = { newProgress ->
                progress = newProgress
                SpotifastNative.seek((newProgress * 180000).toInt())
            }
        )

        // Track / URI controls
        OutlinedTextField(
            value = spotifyUriInput,
            onValueChange = { spotifyUriInput = it },
            label = { Text("Spotify URI / Track ID", fontFamily = FontFamily.Monospace) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = {
                if (spotifyUriInput.isNotEmpty()) {
                    val success = SpotifastNative.playTrack(spotifyUriInput)
                    if (success) {
                        trackTitle = "Playing: $spotifyUriInput"
                        isPlaying = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("LOAD & STREAM VIA LIBRESPOT", fontFamily = FontFamily.Monospace)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Spotifast Android Port - Core: Rust librespot / JNI / MediaSession",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontFamily = FontFamily.Monospace
        )
    }
}
