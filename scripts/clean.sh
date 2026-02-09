#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Cleaning build artifacts ==="
./gradlew clean "$@"
echo "=== Clean complete ==="
