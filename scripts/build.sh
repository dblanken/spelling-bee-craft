#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Building Spelling Bee Craft ==="
./gradlew build "$@"
echo "=== Build complete ==="
echo "Jar: build/libs/$(ls build/libs/*.jar 2>/dev/null | head -1 | xargs basename 2>/dev/null || echo 'not found')"
