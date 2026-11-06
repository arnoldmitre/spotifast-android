package com.spotifast.android

import android.util.Log

object SpotifastNative {
    private const val TAG = "SpotifastNative"

    init {
        try {
            System.loadLibrary("spotifast_core")
            Log.i(TAG, "Successfully loaded native spotifast_core library")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Failed to load native spotifast_core library", e)
        }
    }

    external fun initSession(username: String, password: String): Boolean
    external fun playTrack(uri: String): Boolean
    external fun pauseTrack()
    external fun resumeTrack()
    external fun seek(positionMs: Int)
    external fun isPlaying(): Boolean
    external fun getCurrentUri(): String
}
