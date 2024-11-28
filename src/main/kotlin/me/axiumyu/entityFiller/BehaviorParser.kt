package me.axiumyu.entityFiller

class BehaviorParser(val modes: List<String>) {

    companion object {
        @JvmField
        val shortcut = hashMapOf<String, String>(
            "f" to "force",
            "r" to "replace",
            "s" to "skip",
            "c" to "clear"
        )

        @JvmField
        val default = hashMapOf<String, Boolean>(
            "force" to false,
            "replace" to false,
            "skip" to false,
            "clear" to false
        )
    }

    private val initMap = default.toMutableMap()

    fun parseBehavoir(): Map<String, Boolean>? {
        var modified = false
        modes.forEach {
            if (shortcut.containsKey(it)) {
                initMap[shortcut[it]!!] = true
                modified = true

            } else if (shortcut.values.contains(it)) {
                initMap[it] = true
                modified = true
            }
        }
        return if (modified) initMap else null
    }


}