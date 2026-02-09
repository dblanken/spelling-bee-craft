#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Launching Minecraft Client ==="
./gradlew runClient "$@"
