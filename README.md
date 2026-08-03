# Heads

Heads is a [PowerNukkitX](https://github.com/PowerNukkitX/PowerNukkitX) plugin for creating and placing custom player heads. It supports online Bedrock players, Java Edition usernames, and a browsable collection of decorative heads from [HeadDB](https://headdb.org/).

## Features

- Create a head from an online Bedrock player's current skin
- Fetch a player head by Java Edition username
- Browse decorative heads by category in an in-game form

## Requirements

- A PowerNukkitX server with API 3.0.0

## Installation

1. Download `Heads.jar` from the [latest release](https://github.com/Syodo-Development/Heads/releases/latest).
2. Copy the file into your server's `plugins` directory.
3. Restart the server.

## Commands

| Command | Description |
| --- | --- |
| `/head bedrock <player>` | Creates a head from an online Bedrock player. Persona skins are not supported. |
| `/head java <username>` | Downloads and creates a head for a Java Edition username. |
| `/head database` | Opens the HeadDB category browser. |

All commands can only be used by players and require the `heads.get` permission.

## Screenshots

<p>
  <img width="599" alt="HeadDB category selection" src="https://github.com/user-attachments/assets/518cd257-29e7-412d-94b7-ba360dc9d49d" />
  <img width="693" alt="HeadDB head selection" src="https://github.com/user-attachments/assets/94a1d60d-81bb-4646-8de5-8df6e1ce97b4" />
</p>


## Third-party services

Java player skins are retrieved through [minecraft.tools](https://minecraft.tools/), while the database browser uses [HeadDB](https://headdb.org/). Availability of these features depends on the respective external services.
