mod player;

use std::sync::Arc;
use lazy_static::lazy_static;
use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::{jboolean, jint, jstring};
use log::{info, LevelFilter};
use android_logger::Config;

use player::SpotifastPlayer;

lazy_static! {
    static ref PLAYER: Arc<SpotifastPlayer> = {
        android_logger::init_once(
            Config::default()
                .with_max_level(LevelFilter::Debug)
                .with_tag("SpotifastNative")
        );
        info!("Spotifast native core initialized");
        Arc::new(SpotifastPlayer::new())
    };
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_initSession(
    mut env: JNIEnv,
    _class: JClass,
    username: JString,
    password: JString,
) -> jboolean {
    let user: String = match env.get_string(&username) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };
    let pass: String = match env.get_string(&password) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };

    if PLAYER.authenticate(user, pass) {
        1
    } else {
        0
    }
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_playTrack(
    mut env: JNIEnv,
    _class: JClass,
    uri: JString,
) -> jboolean {
    let track_uri: String = match env.get_string(&uri) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };

    if PLAYER.play(track_uri) {
        1
    } else {
        0
    }
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_pauseTrack(
    _env: JNIEnv,
    _class: JClass,
) {
    PLAYER.pause();
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_resumeTrack(
    _env: JNIEnv,
    _class: JClass,
) {
    PLAYER.resume();
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_seek(
    _env: JNIEnv,
    _class: JClass,
    position_ms: jint,
) {
    PLAYER.seek(position_ms as u32);
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_isPlaying(
    _env: JNIEnv,
    _class: JClass,
) -> jboolean {
    let (is_playing, _, _) = PLAYER.get_playback_state();
    if is_playing { 1 } else { 0 }
}

#[no_mangle]
pub extern "system" fn Java_com_spotifast_android_SpotifastNative_getCurrentUri(
    env: JNIEnv,
    _class: JClass,
) -> jstring {
    let (_, uri_opt, _) = PLAYER.get_playback_state();
    let uri = uri_opt.unwrap_or_default();
    match env.new_string(uri) {
        Ok(jstr) => jstr.into_raw(),
        Err(_) => std::ptr::null_mut(),
    }
}
