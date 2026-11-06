use std::sync::atomic::{AtomicBool, Ordering};
use parking_lot::Mutex;
use log::info;

pub struct SpotifastPlayer {
    pub is_playing: AtomicBool,
    pub current_uri: Mutex<Option<String>>,
    pub position_ms: Mutex<u32>,
    pub runtime: tokio::runtime::Runtime,
}

impl SpotifastPlayer {
    pub fn new() -> Self {
        let rt = tokio::runtime::Builder::new_multi_thread()
            .worker_threads(2)
            .enable_all()
            .build()
            .expect("Failed to initialize tokio runtime for Spotifast");

        Self {
            is_playing: AtomicBool::new(false),
            current_uri: Mutex::new(None),
            position_ms: Mutex::new(0),
            runtime: rt,
        }
    }

    pub fn authenticate(&self, username: String, _password: String) -> bool {
        info!("Authenticating Spotify session for user: {}", username);
        true
    }

    pub fn play(&self, uri: String) -> bool {
        info!("Requesting playback for URI: {}", uri);
        *self.current_uri.lock() = Some(uri);
        self.is_playing.store(true, Ordering::SeqCst);
        true
    }

    pub fn pause(&self) {
        info!("Pausing playback");
        self.is_playing.store(false, Ordering::SeqCst);
    }

    pub fn resume(&self) {
        info!("Resuming playback");
        self.is_playing.store(true, Ordering::SeqCst);
    }

    pub fn seek(&self, position_ms: u32) {
        info!("Seeking to: {} ms", position_ms);
        *self.position_ms.lock() = position_ms;
    }

    pub fn get_playback_state(&self) -> (bool, Option<String>, u32) {
        (
            self.is_playing.load(Ordering::SeqCst),
            self.current_uri.lock().clone(),
            *self.position_ms.lock(),
        )
    }
}
