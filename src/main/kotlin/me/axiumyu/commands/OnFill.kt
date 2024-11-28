package me.axiumyu.commands

import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.NullRegion
import com.sk89q.worldedit.regions.Region
import me.axiumyu.ModeParser
import me.axiumyu.entity.EntityParser
import me.axiumyu.util.Utils.Location
import me.axiumyu.util.Utils.classifyStrings
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player



/**
 * /fillentity [EntityType] <mode> ... <属性>=<值> <属性>=<值> ...
 *
 * 该命令用于填充指定类型的实体，并设置其属性，[]表示必选参数，<>表示可选参数。
 *
 * 参数说明：
 * - [EntityType]: 实体的类型，指定要填充的实体种类。
 * - <mode>: 填充模式，可以指定一个或多个模式来控制填充行为。
 * - <属性>=<值>: 设置实体的属性(区分大小写)及其对应的值，可以指定多个属性和值。
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
 * - 模式的具体含义和行为请参考 `me.axiumyu.entityFiller.BehaviorParser` 类。
 *
 * 示例：
 * /fillentity zombie -c Health=1.0d
 * 该示例命令将在使用WorldEdit创建的选区中清除所有非空气方块，并在每个方块上填充僵尸实体，并设置其生命值为1.0。
 */
object OnFill : CommandExecutor {
    private const val INFO = 0x58ec7f
    private const val ERROR = 0xCC3333
    override fun onCommand(
        p0: CommandSender, p1: Command, p2: String,
        p3: Array<out String>?
    ): Boolean {
        if (p0 !is Player) {
            p0.sendMessage(text().content("仅玩家可执行此命令").color(color(ERROR)))
            return false
        }
        if (p3 == null || p3.isEmpty()) {
            p0.sendMessage(text().content("请输入实体类型").color(color(ERROR)))
            return false
        }

        val region: Region? = validateRegion(p0)
        if(region==null) return false

        val start = region.minimumPoint
        val end = region.maximumPoint

        p0.sendMessage(text().content(formatRegionSize(start, end)).color(color(INFO)))
        p0.sendMessage(text().content("正在填充实体...").color(color(INFO)))
        var skipBlock = 0
        try {
            val list = p3.toMutableList()
            val params = classifyStrings(list)
            val type = EntityParser.initEntity(list.removeAt(0))
            val mode = ModeParser.parseMode(params.first)
            val replace = mode["replace"] == true
            val clear = mode["clear"] == true
            val skip = mode["skip"] == true
            for (i in start.x..end.x) {
                for (j in start.y..end.y) {
                    for (k in start.z..end.z) {
                        val location = Location(p0.world, i, j, k)
                        val sameEntities = p0.world.getNearbyEntities(location, 0.5, 0.5, 0.5) { it.type == type }
                        if (replace && sameEntities.isNotEmpty()) {
                            sameEntities.forEach { it.remove() }
                        }
                        if (!location.block.isEmpty) {
                            if (clear) {
                                location.block.type = Material.AIR
                            } else if (skip) {
                                skipBlock++
                                continue
                            }
                        }

                        val entityPrime = p0.world.spawnEntity(location, type)
                        EntityParser.writeAttribute(entityPrime, params.second)
                    }
                }
            }
        } catch (e: Exception) {
            p0.sendMessage(text().content("发生了错误：${e.javaClass.simpleName}").color(color(ERROR)))
            p0.sendMessage(text().content(e.message ?: "未知错误").color(color(ERROR)))
        }
        val count = (end.x - start.x + 1) * (end.y - start.y + 1) * (end.z - start.z + 1) - skipBlock
        p0.sendMessage(text().content("填充完成，共填充了 $count 个实体").color(color(INFO)))
        return true
    }

    private fun validateRegion(p0: Player): Region? {
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
}