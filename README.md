# 实体填充插件[WIP]

**简体中文** | [English](README-en.md)

## 简介

Entity Filler是一个用于Minecraft的插件，它提供了一个核心命令 `/fillentity`，用于在指定的区域内填充特定类型的实体，并设置这些实体的属性。

插件需要[NBTAPI](https://modrinth.com/plugin/nbtapi)和[WorldEdit](https://modrinth.com/plugin/worldedit)或其分支[FastAsyncWorldEdit](https://modrinth.com/plugin/fastasyncworldedit/)才能正常工作

## 命令使用

### 命令格式

```
/fillentity <EntityType> [fill mode] [position mode] ... [属性]=[值] [属性]=[值] ...
```

### 参数说明

- `<EntityType>`: 必选参数，指定要填充的实体类型。
- `[fill mode]`: 可选参数，指定一个或多个填充模式，用于控制填充行为。
- `[position mode]`: 可选参数，指定一个或多个填充位置，用于控制实体生成时的位置变化。
- `[属性]=[值]`: 可选参数，设置实体的属性及其对应的值，可以指定多个属性和值。

### 数据类型说明

- **Integer List**: 使用 `[I;1,2,3]` 表示整数列表。
- **Long List**: 使用 `[L;1,2,3]` 表示长整型列表。
- **Double List**: 使用 `[D;1.0,2.0,3.0]` 表示双精度浮点数列表。
- **Float List**: 使用 `[F;1.0,2.0,3.0]` 表示单精度浮点数列表。
- **Compound**: 使用 `{}` 表示复合数据类型。
- **String**: 使用 `""` 表示字符串。
- **Boolean**: 使用 `true/false` 表示布尔值。

在数值后加上类型名首字母以指定数值类型，例如：

- `1i` 表示整数
- `1L` 表示长整型
- `1.0d` 表示双精度浮点数
- `1.0f` 表示单精度浮点数

### 模式说明

模式的具体含义和行为请参考 `PlaceModeParser` 和 `PositionMode` 类。

## 示例

```
/fillentity zombie -c Health=1.0d
```

该示例命令将在使用WorldEdit创建的Cuboid选区中清除所有非空气方块，并在每个方块上填充僵尸实体，并设置其生命值为1.0。

## 安装

1. 将插件的JAR文件放入Minecraft服务器的`plugins`文件夹中。
2. 重启服务器或使用插件加载命令加载插件。
3. 确保服务器已安装并启用了WorldEdit插件。

## 贡献

插件仍在开发中，如果您有任何建议或问题，请通过插件的官方GitHub页面提交。

## 许可证

该插件遵循MIT许可证。请查看项目中的LICENSE文件了解更多信息。

---

请注意，本README文档中的命令和参数示例仅供参考，实际使用时请根据您的需求进行调整。
