#!/usr/bin/env bash
set -e

echo "=== Building Spotifast Rust Core for Android ==="

# Check for cargo-ndk
if ! command -v cargo-ndk &> /dev/null; then
    echo "cargo-ndk could not be found. Install it via: cargo install cargo-ndk"
    exit 1
fi

if [ -z "$ANDROID_NDK_HOME" ]; then
    echo "WARNING: ANDROID_NDK_HOME is not set. Please export your NDK path."
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
RUST_CORE_DIR="$PROJECT_ROOT/core-rust"
OUTPUT_DIR="$PROJECT_ROOT/app/src/main/jniLibs"

mkdir -p "$OUTPUT_DIR"

cd "$RUST_CORE_DIR"

echo "Building aarch64-linux-android (arm64-v8a)..."
cargo ndk -t arm64-v8a -o "$OUTPUT_DIR" build --release

echo "Building armv7-linux-androideabi (armeabi-v7a)..."
cargo ndk -t armeabi-v7a -o "$OUTPUT_DIR" build --release

echo "Building x86_64-linux-android (x86_64)..."
cargo ndk -t x86_64 -o "$OUTPUT_DIR" build --release

echo "=== Native build complete! Shared libraries copied to $OUTPUT_DIR ==="
