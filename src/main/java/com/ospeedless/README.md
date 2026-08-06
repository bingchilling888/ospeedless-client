# oSpeedless Client 1.0

Premium legitimate PvP utility client for Minecraft 1.8.9 (Forge 11.15.1.2318).

## Features

- **HUD Modules** (24): FPS, Ping, CPS, Keystrokes, Coordinates, Armor Status, Potion Effects, Combo Counter, Sprint Indicator, Direction, Biome, Time, Memory, Speed, Reach, Health, Server Address, Player Count, Playtime, Active Mods, Arrow/Blocks/Gapple/Potion Counters
- **Render**: Custom Crosshair, Hitbox, Advanced Nametags (with same-client logo support), Block Overlay, Motion Blur
- **Performance**: FPS Boost (presets), Frame Unlock, Fullbright
- **Utility**: Zoom, NoHurtCam, Toggle Sprint/Sneak
- **Click GUI** (Right Shift): Dark + yellow theme, Mods/Settings tabs, module settings, keybind binding
- **HUD Editor** (H): Draggable elements, snapping, guide lines
- **Custom Title Screen** with logo
- **JSON Config** via Gson at `.minecraft/ospeedlessclient/config.json`

## Build Requirements

- **JDK 8** (required)
- **4GB+ RAM** recommended for `setupDecompWorkspace` (FernFlower decompilation)
- Internet for first-time dependency download

```bash
# Set JDK 8
export JAVA_HOME=/path/to/jdk8
export PATH=$JAVA_HOME/bin:$PATH
export GRADLE_OPTS="-Xmx3G"

# Setup (once)
./gradlew setupDecompWorkspace

# Build
./gradlew build
```

Output JAR: `build/libs/ospeedlessclient-1.0.jar`

## Controls

| Key | Action |
|-----|--------|
| Right Shift | Open Client Settings (Click GUI) |
| H | Open HUD Editor |
| Module keybinds | Toggle individual modules (configurable) |

## Package

`com.ospeedless` · Mod ID `ospeedlessclient` · Theme: Yellow

Fully legit. Client-sided visuals, HUD, QoL and performance only. No cheats.
