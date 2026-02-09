#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "=== Running Tests ==="
./gradlew test "$@"

# Show summary from test report if available
REPORT="build/reports/tests/test/index.html"
if [ -f "$REPORT" ]; then
    echo ""
    echo "Test report: file://$(pwd)/$REPORT"
fi
