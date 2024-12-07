package me.axiumyu.util

import me.axiumyu.parser.LocationModeParser
import me.axiumyu.parser.LocationModeParser.parseLocationMode
import org.bukkit.Location
import org.bukkit.World

/**
 * 工具类
 */
object Utils {

    /*@JvmStatic
    fun BlockVector3.toLocation(world: World) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    @JvmStatic
    fun BlockVector3.toBlockLocation(world: World) = Location(world, blockX.toDouble(), blockY.toDouble(), blockZ.toDouble())

    @JvmStatic
    fun Location.toBlockCenter() = this.toBlockLocation().add(0.5, 0.5, 0.5)*/

    @JvmStatic
    fun Location(world: World, x: Int, y: Int, z: Int) : Location{
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    @JvmStatic
    fun splitByEquals(input: String): List<String> {
        val result = mutableListOf<String>()
        val currentPart = StringBuilder()
        var skipNext = false

        for (i in input.indices) {
            val char = input[i]

            // 如果检测到反斜杠且未跳过，跳过下一个字符
            if (char == '\\' && !skipNext) {
                skipNext = true
            } else if (char == '=' && !skipNext) {
                // 如果是未转义的 "="，分割
                result.add(currentPart.toString())
                currentPart.clear()
            } else {
                // 否则加入当前部分
                currentPart.append(char)
                skipNext = false
            }
        }

        // 添加最后一部分
        result.add(currentPart.toString())
        return result
    }
    
    @JvmStatic
    fun classifyStrings(strings: List<String>): ParamGroup {
        // 使用 partition 将列表分为三组
        val (pos, pla, att) = strings.partition { it.startsWith("--") }.let { (ddp, rest) ->
            val (dp, kvp) = rest.partition { it.startsWith("-") }
            Triple(ddp, dp, kvp)
        }

        // 检查带有 "--" 前缀的参数是否只有一个
        if (pos.size > 1) {
            throw InvalidFormatException("只能有一个位置参数")
        }

        // 提取带有 "--" 前缀的参数
        val positionMode = pos.firstOrNull()
        val posMode = parseLocationMode(positionMode?.substringAfter("--") ?: "DOWN")

        // 解析形如 "键=值" 的字符串
        val parsedKeyValue = att
            .filter { it.contains("=") }
            .map {
                val parts = splitByEquals(it)
                if (parts.size == 2) {
                    parts[0] to parts[1]  // 返回键值对
                } else {
                    throw InvalidFormatException("字符串 '$it' 格式不符合 '键=值' 规则")
                }
            }

        // 检查剩下的没有被分类的字符串
        val invalidStrings = strings.filter {
            !it.startsWith("-") && !it.contains("=")
        }

        // 如果有不符合格式的字符串，则抛出异常
        if (invalidStrings.isNotEmpty()) {
            throw InvalidFormatException("存在不符合格式的字符串: $invalidStrings")
        }

        return ParamGroup(posMode, pla, parsedKeyValue)
    }

    @JvmStatic
    fun Location.setPosition(positionMode: LocationModeParser.PositionMode): Location {
        return this.add(positionMode.x, positionMode.y, positionMode.z)
    }

    data class ParamGroup(
        val positionMode: LocationModeParser.PositionMode,
        val placeModes: List<String>,
        val attributes: List<Pair<String, String>>
    )
    
    // 自定义异常类
    class InvalidFormatException(message: String) : Exception(message)
}