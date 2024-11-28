package me.axiumyu.entityFiller.entity

import de.tr7zw.nbtapi.NBT
import me.axiumyu.entityFiller.BehaviorParser
import me.axiumyu.entityFiller.util.Utils
import me.axiumyu.entityFiller.util.Utils.classifyStrings
import me.axiumyu.entityFiller.util.Utils.splitByEquals
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import kotlin.collections.toByteArray

class EntityParser(params: Array<out String>) {
    val param : MutableList<String> = params.toMutableList()
    val type = param.removeAt(0)


    fun initEntity(): EntityType {
        val entityType = EntityType.fromName(type)
        if (entityType == null) throw IllegalArgumentException("Invalid entity type")
        return entityType
    }

    fun parseModes(): List<String> {

    }

    @Throws(NumberFormatException::class)
    fun writeAttribute(entity: Entity): Entity {
        val filterResult = classifyStrings(param)
        val modes = filterResult.first
        val attributes = filterResult.second



        param.forEach {

            val modelist by lazy{ mutableListOf<String>() }
            if (it.startsWith("-")) {
                modelist.add(it.substring(1,it.length))
                return@forEach
            }
            val mp by lazy{ BehaviorParser(modelist) }
            val modes = mp.parseBehavoir()

            val split = splitByEquals(it)
            val noSuffix by lazy{ split[1].substring(0,split[1].length-1) }
            val split1 by lazy { split[1].substring(3, split[1].length - 1).split(",") }
            if (split.size == 2) {
                split[1].run {
                    NBT.modify(entity) { pr ->
                        if (startsWith("[L;") && endsWith("]")) {
                            pr.setLongArray(split[0], split1.map { toLong() }.toLongArray())

                        } else if (startsWith("[I;") && endsWith("]")) {
                            pr.setIntArray(split[0], split1.map { toInt() }.toIntArray())

                        } else if (startsWith("[B;") && endsWith("]")) {
                            pr.setByteArray(split[0], split1.map { toByte() }.toByteArray())

                        } else if (lowercase() == "true" || lowercase() == "false") {
                            pr.setBoolean(split[0], toBoolean())

                        } else if (endsWith("b", true)) {
                            pr.setByte(split[0], noSuffix.toByte())

                        } else if (endsWith("s", true)) {
                            pr.setShort(split[0], noSuffix.toShort())

                        } else if (endsWith("i", true)) {
                            pr.setInteger(split[0], noSuffix.toInt())

                        } else if (endsWith("l", true)) {
                            pr.setLong(split[0], noSuffix.toLong())

                        } else if (endsWith("f", true)) {
                            pr.setFloat(split[0], noSuffix.toFloat())

                        } else if (endsWith("d", true)) {
                            pr.setDouble(split[0], noSuffix.toDouble())

                        } else if (startsWith("\"") && endsWith("\"")) {
                            pr.setString(split[0], substring(1, length - 1))
                        } else if (startsWith("{") && endsWith("}")) {
                            pr.mergeCompound(NBT.parseNBT(it))
                        }
                    }
                }
            } else throw IllegalArgumentException("Invalid attribute format")
        }
        return entity
    }
}

