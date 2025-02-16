package me.axiumyu.commands

import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.NullRegion
import com.sk89q.worldedit.regions.Region
import me.axiumyu.ConfigFile
import me.axiumyu.entity.EntityParser
import me.axiumyu.parser.LocationModeParser.PositionMode.*
import me.axiumyu.parser.PlaceModeParser.parsePlaceMode
import me.axiumyu.util.Utils.Location
import me.axiumyu.util.Utils.classifyStrings
import me.axiumyu.util.Utils.setPosition
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.util.BoundingBox

/**
 * `/fillentity <EntityType> [mode] ... [属性]=[值] [属性]=[值] ...`
 *
 * 该命令用于填充指定类型的实体，并设置其属性，<>表示必选参数，[]表示可选参数。
 *
 * 参数说明：
 * - <EntityType>: 实体的类型，指定要填充的实体种类。
 * - [mode]: 填充模式，可以指定一个或多个模式来控制填充行为。
 * - [属性]=[值]: 设置实体的属性(区分大小写)及其对应的值，可以指定多个属性和值。
 *
 * 数据类型说明：
 * - Integer List: 使用 [I;1,2,3] 表示整数列表。
 * - Long List: 使用 [L;1,2,3] 表示长整型列表。
 * - Double List: 使用 [D;1.0,2.0,3.0] 表示双精度浮点数列表。
 * - Float List: 使用 [F;1.0,2.0,3.0] 表示单精度浮点数列表。
 * - Compound: 使用 {} 表示复合数据类型。
 * - String: 使用 "" 表示字符串。
 * - Boolean: 使用 true/false 表示布尔值。
 *
 * 数值类型说明：
 * - 在数值后加上类型名首字母以指定数值类型，例如：1i 表示整数，1L 表示长整型，1.0d 表示双精度浮点数，1.0f 表示单精度浮点数。
 *
 * 模式说明：
 * - 模式的具体含义和行为请参考 `PlaceModeParser` 和 `LocationModeParser` 类。
 *
 * 示例：
 * `/fillentity zombie -c Health=1.0d`
 * 该示例命令将在使用WorldEdit创建的Cuboid选区中清除所有非空气方块，并在每个方块上填充僵尸实体，并设置其生命值为1.0。
 */
object OnFill : CommandExecutor {

    const val INFO = 0x58ec7f
    const val ERROR = 0xCC3333

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        val list = p3.toMutableList()
        if (p0 !is Player) {
            p0.sendMessage(text().content("仅玩家可执行此命令").color(color(ERROR)))
            return false
        }
        if (!p0.hasPermission("entityfiller.command.use")) {
            p0.sendMessage(text().content("你没有权限执行此命令").color(color(ERROR)))
            return true
        }
        if (p3.isEmpty()) {
            p0.sendMessage(text().content("请输入实体类型").color(color(ERROR)))
            return false
        }
        val type = EntityType.valueOf(list.removeAt(0).uppercase())

        val region: Region = getValidRegion(p0) ?: return false

        val start = region.minimumPoint
        val end = region.maximumPoint
        val size = getRegionFullSize(start, end)
        if (size > ConfigFile.maxFillCount) {
            p0.sendMessage(text().content("填充区域过大，请缩小范围").color(color(ERROR)))
            return false
        }

        p0.sendMessage(text().content(formatRegionSize(start, end)).color(color(INFO)))
        p0.sendMessage(text().content("正在填充实体...").color(color(INFO)))
        var skipBlock = 0
        try {
            val params = classifyStrings(list)

            val placeMode = parsePlaceMode(params.placeModes)
            val replace = placeMode.contains("replace")
            val clear = placeMode.contains("clear")
            val skip = placeMode.contains("skip")

            val position = params.positionMode

//            val trueKeys = placeMode.filter { it.value }.keys.toList().joinToString()
            p0.sendMessage(text().content("填充模式：replace:$replace , clear:$clear , skip:$skip").color(color(INFO)))
            p0.sendMessage(text().content("属性:${params.attributes}, 填充位置:${position}"))
            val sameEntities = getRegionEntities(region, type)
            if (replace && sameEntities.isNotEmpty()) {
                sameEntities.forEach { it.remove() }
                p0.sendMessage(text().content("已删除 $type 实体 ${sameEntities.size} 个").color(color(INFO)))
            }
            for (i in start.x..end.x) {
                for (j in start.y..end.y) {
                    for (k in start.z..end.z) {
                        val location = Location(p0.world, i, j, k)
                        if (!location.block.isEmpty) {
                            if (clear) {
                                location.block.type = Material.AIR
                            }
                            if (skip) {
                                skipBlock++
                                continue
                            }
                        }
                        val entityPrime = p0.world.spawnEntity(
                            location.setPosition(position),
                            type,
                            CreatureSpawnEvent.SpawnReason.COMMAND
                        )
                        EntityParser.writeAttribute(entityPrime, params.attributes)
                    }
                }
            }
        } catch (e: Exception) {
            p0.sendMessage(text().content("发生了错误：${e.javaClass.simpleName}").color(color(ERROR)))
            p0.sendMessage(text().content(e.message ?: "未知错误").color(color(ERROR)))
        }
        p0.sendMessage(text().content("填充完成，共填充了 ${size - skipBlock} 个实体").color(color(INFO)))
        return true
    }

    private fun getRegionFullSize(start: BlockVector3, end: BlockVector3): Int =
        (end.x - start.x + 1) * (end.y - start.y + 1) * (end.z - start.z + 1)

    private fun getValidRegion(p0: Player): Region? {
        val localSession = WorldEdit.getInstance().sessionManager.get(BukkitAdapter.adapt(p0))
        val region = try {
            localSession.getSelection(localSession.selectionWorld)
        } catch (_: IncompleteRegionException) {
            NullRegion()
        }
        if (region !is CuboidRegion) {
            p0.sendMessage(text().content("请使用WorldEdit选择一个Cuboid区域").color(color(ERROR)))
            return null
        }
        return region
    }

    private fun formatRegionSize(start: BlockVector3, end: BlockVector3): String {
        return "区域大小：${start.x}, ${start.y}, ${start.z} 到 ${end.x}, ${end.y}, ${end.z}"
    }

    private fun getRegionEntities(region: Region, entityType: EntityType): MutableCollection<Entity> {
        val max = region.maximumPoint
        val min = region.minimumPoint
        val boundBox = BoundingBox(
            min.x.toDouble(),
            min.y.toDouble(),
            min.z.toDouble(),
            max.x.toDouble(),
            max.y.toDouble(),
            max.z.toDouble()
        )
        val center = Location(
            BukkitAdapter.adapt(region.world),
            (max.x + min.x) / 2.0,
            (max.y + min.y) / 2.0,
            (max.z + min.z) / 2.0
        )
        return center.world.getNearbyEntities(
        boundBox) { it.type == entityType }
    }
}