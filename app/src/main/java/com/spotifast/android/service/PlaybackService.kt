package com.spotifast.android.service

import android.content.Intent
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.spotifast.android.SpotifastNative

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize ExoPlayer or custom player forwarder
        player = ExoPlayer.Builder(this).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        SpotifastNative.resumeTrack()
                    } else {
                        SpotifastNative.pauseTrack()
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.mediaId?.let { uri ->
                        SpotifastNative.playTrack(uri)
                    }
                }
            })
        }

        player?.let {
            mediaSession = MediaSession.Builder(this, it)
                .setCallback(CustomMediaSessionCallback())
                .build()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private inner class CustomMediaSessionCallback : MediaSession.Callback {
        override fun onPlaybackResumption(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo
        ): com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.MediaSession.MediaItemsWithStartPosition> {
            SpotifastNative.resumeTrack()
            return super.onPlaybackResumption(mediaSession, controller)
        }
    }
}
