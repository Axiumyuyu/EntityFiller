package me.axiumyu.parser

import kotlin.random.Random

/**
* Position Modes(需要带上`--`前缀):
*
* - c - center : 实体生成于方块中心
*
* - d - down ： 实体生成于向下取整坐标
*
* - r - random : 实体生成于方块内随机坐标
*
* - u - up : 实体生成于向上取整坐标
*
*/
object LocationModeParser {
    @JvmField
    val shortcut = hashMapOf (
        "u" to "up",
        "r" to "random",
        "d" to "down",
        "c" to "center"
    )

    @JvmStatic
    fun parseLocationMode(mode: String): PositionMode {
        if (shortcut.containsKey(mode)) {
            return PositionMode.valueOf(shortcut[mode]!!)
        } else if (shortcut.values.contains(mode)){
            return PositionMode.valueOf(mode)
        }
        return PositionMode.DOWN
    }

    enum class PositionMode(val x: Double, val y: Double, val z: Double) {
        UP(1.0,1.0,1.0),
        RANDOM(Random.nextDouble(),Random.nextDouble(),Random.nextDouble()),
        DOWN(0.0,0.0,0.0),
        CENTER(0.5,0.5,0.5)
    }
}
