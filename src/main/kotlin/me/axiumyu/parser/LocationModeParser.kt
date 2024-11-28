package me.axiumyu.parser

import kotlin.random.Random

/**
* Position Modes(需要带上`--`前缀):
*
* - c - center : 实体生成于方块中心(TODO)
*
* - d - down ： 实体生成于向下取整坐标(TODO)
*
* - r - random : 实体生成于方块内随机坐标(TODO)
*
* - u - up : 实体生成于向上取整坐标(TODO)
*
*/

object LocationModeParser {
    @JvmField
    val shortcut = hashMapOf<String, String>(
        "u" to "up",
        "r" to "random",
        "d" to "down",
        "c" to "center"
    )


    enum class PostionMode(val x: Double,val y: Double,val z: Double) {
        UP(1.0,1.0,1.0),
        RANDOM(Random.nextDouble(),Random.nextDouble(),Random.nextDouble()),
        DOWN(0.0,0.0,0.0),
        CENTER(0.5,0.5,0.5)
    }
}
