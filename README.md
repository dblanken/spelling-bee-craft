# Spelling Bee Craft

A Fabric mod for Minecraft 1.21.1 that modifies creative mode search to require **complete word spelling**. Items only appear when every word you type is a full, correctly spelled word from the item's name. Designed as an educational tool to help kids practice spelling while playing Minecraft.

## How It Works

In vanilla Minecraft, typing "dia" in the creative search tab shows all diamond items. With Spelling Bee Craft enabled, you must type the **complete word** "diamond" before those items appear.

- Words can be typed in any order ("egg zombie" finds "Zombie Spawn Egg")
- You don't need to type every word ("zombie egg" also finds "Zombie Spawn Egg")
- Hyphens are treated as spaces ("cave spider" matches "Cave-Spider")
- Empty search shows all items as usual

### Difficulty Modes

| Mode | Behavior | Example |
|------|----------|---------|
| **Easy** | Fuzzy matching — small typos and near-complete words are accepted | "zombi" matches "Zombie" |
| **Medium** (default) | Exact words, case-insensitive | "zombie" matches "Zombie" |
| **Hard** | Exact words, case-sensitive | Only "Zombie" matches "Zombie" |

## Commands

All commands are client-side slash commands:

| Command | Description |
|---------|-------------|
| `/spellingbee` | Show current status (enabled/disabled, difficulty) |
| `/spellingbee on` | Enable the mod |
| `/spellingbee off` | Disable the mod (restores vanilla search) |
| `/spellingbee difficulty easy` | Set difficulty to Easy |
| `/spellingbee difficulty medium` | Set difficulty to Medium |
| `/spellingbee difficulty hard` | Set difficulty to Hard |
| `/spellingbee help` | Show help message in chat |

## Visual Feedback

When the mod is active and you're on the creative search tab:

- **Colored border** around the search box: gray (typing), green (matches found), red (no matches)
- **Status message** below the search box showing match count or hint text
- **Difficulty indicator** above the search box showing the current mode

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API 0.102.0+1.21.1
- Java 21

## Building

```bash
# Build the mod (compiles + runs tests + produces jar)
scripts/build.sh

# Or directly with Gradle
./gradlew build
```

The output jar will be in `build/libs/`.

## Running Tests

```bash
# Run unit tests
scripts/test.sh

# Or directly with Gradle
./gradlew test
```

Tests cover the core search logic (WordMatcher and ItemFilter) with no Minecraft dependency.

## Running the Client

```bash
# Launch Minecraft with the mod loaded
scripts/run-client.sh

# Or directly with Gradle
./gradlew runClient
```

## Testing In-Game

1. Launch the client with `scripts/run-client.sh`
2. Create a new world in **Creative mode**
3. Open your inventory (E key) and click the **Search tab** (compass icon)
4. Try typing in the search box:
   - Type "oak" — you should see Oak Planks, Oak Stairs, Oak Door, etc.
   - Type "oak sta" — nothing should appear (partial word "sta")
   - Type "oak stairs" — only Oak Stairs should appear
   - Clear the search — all items return
5. Test commands by pressing T to open chat:
   - Type `/spellingbee` to see the current status
   - Type `/spellingbee difficulty easy` then search for "zombi" — Zombie items should appear
   - Type `/spellingbee difficulty hard` then search for "zombie" (lowercase) — nothing appears; "Zombie" (capitalized) works
   - Type `/spellingbee off` to disable and confirm vanilla search behavior returns

## Cleaning

```bash
scripts/clean.sh
```

## Project Structure

```
spelling-bee-craft/
├── build.gradle                  # Fabric Loom build config
├── gradle.properties             # Version pins (MC, Fabric, Loader)
├── settings.gradle               # Plugin repositories
├── scripts/
│   ├── build.sh                  # Build the mod
│   ├── test.sh                   # Run unit tests
│   ├── clean.sh                  # Clean build artifacts
│   └── run-client.sh             # Launch Minecraft client
├── src/
│   ├── main/                     # Shared code (no Minecraft client deps)
│   │   ├── java/com/spellingbeecraft/
│   │   │   ├── SpellingBeeCraftMod.java       # Mod entrypoint, static state
│   │   │   ├── config/
│   │   │   │   ├── ConfigManager.java         # JSON config persistence
│   │   │   │   ├── DifficultyMode.java        # EASY / MEDIUM / HARD enum
│   │   │   │   └── ModConfig.java             # Config data class
│   │   │   └── search/
│   │   │       ├── ItemFilter.java            # Generic list filtering
│   │   │       ├── SearchResult.java          # Search status holder
│   │   │       └── WordMatcher.java           # Core word-matching logic
│   │   └── resources/
│   │       ├── fabric.mod.json
│   │       ├── spellingbeecraft.mixins.json
│   │       └── assets/spellingbeecraft/lang/en_us.json
│   ├── client/                   # Client-only code
│   │   ├── java/com/spellingbeecraft/
│   │   │   ├── SpellingBeeCraftClient.java    # Client entrypoint, commands
│   │   │   ├── mixin/client/
│   │   │   │   └── CreativeInventoryScreenMixin.java  # Search interception
│   │   │   └── search/
│   │   │       └── FeedbackRenderer.java      # Visual feedback overlay
│   │   └── resources/
│   │       └── spellingbeecraft.client.mixins.json
│   └── test/                     # Unit tests (pure Java, no MC)
│       └── java/com/spellingbeecraft/search/
│           ├── WordMatcherTest.java
│           └── ItemFilterTest.java
└── spelling-bee-craft-prd.md     # Product requirements document
```

## Configuration

The mod saves its config to `config/spellingbeecraft.json` in your Minecraft directory. Settings persist across sessions. The config file is created automatically on first run with these defaults:

```json
{
  "enabled": true,
  "defaultDifficulty": "MEDIUM",
  "showStatusMessages": true,
  "enableColorFeedback": true,
  "searchMode": "CONTINUOUS",
  "fuzzyMatchThreshold": 0.85
}
```

## License

MIT
