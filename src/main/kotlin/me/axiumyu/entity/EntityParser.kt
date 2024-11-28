package me.axiumyu.entity

import de.tr7zw.nbtapi.NBT
import me.axiumyu.ModeParser
import me.axiumyu.util.Utils.classifyStrings
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

class EntityParser(params: Array<out String>) {
    val param = params.toMutableList()
    val type = param.removeAt(0)
    lateinit var modes : Map<String, Boolean>

    fun initEntity(): EntityType {
        val entityType = EntityType.fromName(type)
        if (entityType == null) throw IllegalArgumentException("Invalid entity type: $type")
        return entityType
    }



    @Throws(NumberFormatException::class)
    fun writeAttribute(entity: Entity): Entity {
        val filterResult = classifyStrings(param)
        val modes = filterResult.first
        val attributes = filterResult.second
        this.modes = ModeParser.parseMode(modes)

        attributes.forEach {
            val key = it.first
            val value = it.second

            value.run {
                val noSuffix by lazy { substring(0, length - 1) }
                val listContent by lazy { substring(3, length - 1).split(",") }
                NBT.modify(entity) { pr ->
                    if (startsWith("[L;") && endsWith("]")) {
                        pr.setLongArray(key, listContent.map { toLong() }.toLongArray())

                    } else if (startsWith("[I;") && endsWith("]")) {
                        pr.setIntArray(key, listContent.map { toInt() }.toIntArray())

                    } else if (startsWith("[B;") && endsWith("]")) {
                        pr.setByteArray(key, listContent.map { toByte() }.toByteArray())

                    } else if (lowercase() == "true" || lowercase() == "false") {
                        pr.setBoolean(key, toBoolean())

                    } else if (endsWith("b", true)) {
                        pr.setByte(key, noSuffix.toByte())

                    } else if (endsWith("s", true)) {
                        pr.setShort(key, noSuffix.toShort())

                    } else if (endsWith("i", true)) {
                        pr.setInteger(key, noSuffix.toInt())

                    } else if (endsWith("l", true)) {
                        pr.setLong(key, noSuffix.toLong())

                    } else if (endsWith("f", true)) {
                        pr.setFloat(key, noSuffix.toFloat())

                    } else if (endsWith("d", true)) {
                        pr.setDouble(key, noSuffix.toDouble())

                    } else if (startsWith("\"") && endsWith("\"")) {
                        pr.setString(key, substring(1, length - 1))     // remove quotes
                    } else if (startsWith("{") && endsWith("}")) {
                        pr.mergeCompound(NBT.parseNBT(this))
                    }
                }
            }
        }
        return entity
    }
}
