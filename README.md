# Ancient City Weapons

A Minecraft Spigot/Paper plugin for version 1.21+ that adds Ancient City themed weapons with unique abilities.

## Features

### Warden Beam (Disc Fragment)
- **Base Item:** Disc Fragment
- **Activation:** Right-click
- **Effect:** Fires a dark blue/black themed beam with soul fire particles
  - 4 block reach
  - 1 block wide
  - Deals 3 hearts (6 HP) of damage to entities hit
- **Cooldown:** 1 minute 30 seconds (90 seconds)

### Barrier Cage (Heavy Core)
- **Base Item:** Heavy Core
- **Activation:** Right-click
- **Effect:** Creates a protective barrier cage around the player
  - 4 block radius spherical cage made of barrier blocks
  - Lasts for 10 seconds
  - Automatically removed after duration expires
- **Cooldown:** 1 minute (60 seconds)

## Requirements

- Java 17 or higher (compatible with Java 22)
- Maven 3.6+
- Spigot/Paper server 1.21+

## Building

1. Clone the repository:
   ```bash
   git clone https://github.com/Jonas1903/ancient-city-weapons.git
   cd ancient-city-weapons
   ```

2. Build with Maven:
   ```bash
   mvn clean package
   ```

3. The compiled JAR will be in the `target/` directory:
   ```
   target/AncientCityWeapons-1.0.0.jar
   ```

## Installation

1. Build the plugin or download the JAR from releases
2. Place the JAR file in your server's `plugins/` folder
3. Restart or reload your server

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/ancientweapons give beam` | Get the Warden Beam item | `ancientweapons.give` |
| `/ancientweapons give cage` | Get the Barrier Cage item | `ancientweapons.give` |

**Aliases:** `/aw`, `/acw`

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `ancientweapons.give` | Allows giving Ancient City weapons | OP only |
| `ancientweapons.use` | Allows using Ancient City weapons | Everyone |

## Development

### Project Structure

```
ancient-city-weapons/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        ├── java/
        │   └── com/ancientcity/weapons/
        │       ├── AncientCityWeapons.java      # Main plugin class
        │       ├── commands/
        │       │   └── AncientWeaponsCommand.java
        │       ├── listeners/
        │       │   └── ItemListener.java
        │       └── managers/
        │           ├── BarrierCageManager.java
        │           ├── CooldownManager.java
        │           └── ItemManager.java
        └── resources/
            └── plugin.yml
```

### Opening in IntelliJ IDEA

1. Open IntelliJ IDEA
2. Select "Open" and navigate to the project directory
3. IntelliJ will automatically detect the Maven project
4. Wait for Maven to index and download dependencies
5. You're ready to develop!

## License

This project is open source. Feel free to use and modify as needed.