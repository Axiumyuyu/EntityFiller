package me.axiumyu.parser

/**
 * Place Modes(需要带上`-`前缀):
 *
 * - f - force : (默认模式)强制放置实体，忽略方块，实体
 *
 * - r - replace : 替换已有实体
 *
 * - s - skip : 跳过方块
 *
 * - c - clear : 清除选区内方块后放置实体
 */

object PlaceModeParser {

    @JvmField
    val shortcut = hashMapOf<String, String>(
        "f" to "force",
        "r" to "replace",
        "s" to "skip",
        "c" to "clear"
    )

    @JvmField
    val default = hashMapOf<String, Boolean>(
        "force" to true,        //default
        "replace" to false,
        "skip" to false,        //覆盖"force"
        "clear" to false
    )

    @JvmStatic
    fun parsePlaceMode(placeModes: List<String>): Map<String, Boolean> {
        val initMap = default.toMutableMap()
        placeModes.forEach {
            if (shortcut.containsKey(it)) {
                initMap[shortcut[it]!!] = true
            } else if (shortcut.values.contains(it)) {
                initMap[it] = true
            }
        }
        return initMap
    }


}