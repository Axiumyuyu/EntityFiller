# Entity Filler Plugin [WIP]

[简体中文](README.md) | **English**

## Introduction

Entity Filler is a Minecraft plugin that provides a core command `/fillentity` for filling a specified area with a certain type of entities and setting their attributes.

Plugin needs [NBTAPI](https://modrinth.com/plugin/nbtapi) and [WorldEdit](https://modrinth.com/plugin/worldedit) or its fork like [FastAsyncWorldEdit](https://modrinth.com/plugin/fastasyncworldedit/) to run


## Command Usage

### Command Format

```
/fillentity <EntityType> [mode] ... [property]=[value] [property]=[value] ...
```

### Parameter Description

- `<EntityType>`: Required parameter, specifies the type of entity to fill.
- `[mode]`: Optional parameter, specifies one or more fill modes to control the fill behavior.
- `[property]=[value]`: Optional parameter, sets the properties and their corresponding values for the entities, multiple properties and values can be specified.

### Data Type Description

- **Integer List**: Use `[I;1,2,3]` to represent a list of integers.
- **Long List**: Use `[L;1,2,3]` to represent a list of long integers.
- **Double List**: Use `[D;1.0,2.0,3.0]` to represent a list of double-precision floating-point numbers.
- **Float List**: Use `[F;1.0,2.0,3.0]` to represent a list of single-precision floating-point numbers.
- **Compound**: Use `{}` to represent a compound data type.
- **String**: Use `""` to represent a string.
- **Boolean**: Use `true/false` to represent a boolean value.

### Numeric Type Description

Append the first letter of the type name to the number to specify the numeric type, for example:
- `1i` represents an integer
- `1L` represents a long integer
- `1.0d` represents a double-precision floating-point number
- `1.0f` represents a single-precision floating-point number

### Mode Description

For the specific meanings and behaviors of modes, please refer to the `PlaceModeParser` and `PositionMode` class.

## Examples

```
/fillentity zombie -c Health=1.0d
```

This example command will clear all non-air blocks in the Cuboid selection created by WorldEdit and fill each block with a zombie entity, setting its health to 1.0.

## Installation

1. Place the plugin's JAR file into the `plugins` folder of your Minecraft server.
2. Restart the server or use the plugin loading command to load the plugin.
3. Ensure that the WorldEdit plugin is installed and enabled on the server.

## Contribution

The plugin is still under development. If you have any suggestions or issues, please submit them through the official GitHub page of the plugin.

## License

This plugin follows the MIT License. Please check the LICENSE file in the project for more information.

---

Please note that the commands and parameter examples in this README document are for reference only. Adjust them according to your needs when using them in practice.

```
