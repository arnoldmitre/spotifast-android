package com.spotifast.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotifast.android.ui.theme.*

@Composable
fun WinampMiniPlayer(
    trackTitle: String,
    bitrateKbps: Int = 320,
    sampleRateKhz: Int = 44,
    isPlaying: Boolean,
    progress: Float,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(WinampMetalBackground)
            .border(2.dp, WinampMetalBorder, RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        // Top Winamp Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SPOTIFAST 2026 // WINAMP MODE",
                fontSize = 10.sp,
                color = WinampAccentAmber,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "STEREO",
                fontSize = 10.sp,
                color = WinampLcdGreen,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Retro Green LCD Screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(WinampLcdBackground)
                .border(1.dp, WinampMetalDark, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isPlaying) "▶ PLAY" else "❚❚ PAUSE",
                        color = WinampLcdGreen,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$bitrateKbps kbps  $sampleRateKhz kHz",
                        color = WinampLcdDimGreen,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = trackTitle.ifEmpty { "1. SPOTIFAST - WAITING FOR TRACK..." },
                    color = WinampLcdGreen,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress Slider
        Slider(
            value = progress,
            onValueChange = onSeek,
            colors = SliderDefaults.colors(
                thumbColor = WinampAccentAmber,
                activeTrackColor = WinampLcdGreen,
                inactiveTrackColor = WinampMetalDark
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Hardware-style Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RetroButton(label = "⏮ PREV", onClick = onPrevious)
            RetroButton(label = if (isPlaying) "❚❚ PAUSE" else "▶ PLAY", onClick = onPlayPause)
            RetroButton(label = "⏭ NEXT", onClick = onNext)
            RetroButton(label = "■ STOP", onClick = onPlayPause)
        }
    }
}

@Composable
fun RetroButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = WinampButtonFace,
            contentColor = WinampLcdGreen
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, WinampButtonHighlight),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}
