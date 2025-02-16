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
    val shortcut = hashMapOf(
        "f" to "force",
        "r" to "replace",
        "s" to "skip",
        "c" to "clear"
    )

    @JvmStatic
    fun parsePlaceMode(placeModes: List<String>): HashSet<String> {
        val initMap = hashSetOf<String>()
        placeModes.forEach {
            if (shortcut.containsKey(it)) {
                initMap.add(shortcut[it]!!)
            } else if (shortcut.values.contains(it)) {
                initMap.add(it)
            }
        }
        return initMap
    }
}