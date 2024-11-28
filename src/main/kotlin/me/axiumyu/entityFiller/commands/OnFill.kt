package me.axiumyu.entityFiller.commands

import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.regions.CuboidRegion
import com.sk89q.worldedit.regions.NullRegion
import com.sk89q.worldedit.regions.Region
import me.axiumyu.entityFiller.entity.EntityParser
import me.axiumyu.entityFiller.util.Utils
import me.axiumyu.entityFiller.util.Utils.Location
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object OnFill : CommandExecutor {

    /*
    * /fillentity [EntityType] <属性>=<值> <属性>=<值> ...
    * Integer List 使用[I;1,2,3]
    * Long List 使用[L;1,2,3]
    * Double List 使用[D;1.0,2.0,3.0]
    * Float List 使用[F;1.0,2.0,3.0]
    * Compound 使用{}
    * String 使用""
    * Boolean 使用true/false
    */
    override fun onCommand(
        p0: CommandSender, p1: Command, p2: String,
        p3: Array<out String>?
    ): Boolean {
        if (p0 !is Player) {
            p0.sendMessage(text().content("仅玩家可执行此命令").color(color(0x58ec7f)))
            return false
        }
        if (p3 == null || p3.isEmpty()) {
            p0.sendMessage(text().content("请输入实体类型").color(color(0x58ec7f)))
            return false
        }
        val region: Region = try {
            val localSession = WorldEdit.getInstance().sessionManager.get(BukkitAdapter.adapt(p0))
            localSession.getSelection(localSession.selectionWorld)
        } catch (_: IncompleteRegionException) {
            NullRegion()
        }
//        p0.sendMessage("区域类型为:${region::class.simpleName}")
        val ep = EntityParser(p3)
        val type = ep.initEntity()

        if (region !is CuboidRegion) {
            p0.sendMessage(text().content("请使用WorldEdit选择一个Cuboid区域").color(color(0x58ec7f)))
            return false
        }
        val start = region.minimumPoint
        val end = region.maximumPoint

        p0.sendMessage(
            text().content(
                "区域大小：${start.x}, ${start.y}, ${start.z} 到 ${end.x}, ${end.y}, ${end.z}"
            ).color(color(0x58ec7f))
        )
        p0.sendMessage(text().content("正在填充实体...").color(color(0x58ec7f)))
        for (i in start.x..end.x) {
            for (j in start.y..end.y) {
                for (k in start.z..end.z) {
                    val location = Location(p0.world, i, j, k)
                    if (p0.world.getNearbyEntities(location, 0.5, 0.5, 0.5) { it.type == type }.isNotEmpty() && ) {

                    }
                    if (!location.block.isEmpty) {

                    }
                    try {
                        val entityPrime = p0.world.spawnEntity(location, type)
                        ep.writeAttribute(entityPrime)
                    } catch (e: Utils.InvalidFormatException) {
                        p0.sendMessage(text().content("发生了错误："))
                        p0.sendMessage(text().content(e.message ?: "未知错误").color(color(0x58ec7f)))
                    }
                }
            }
        }
        p0.sendMessage(text().content("填充完成，共填充了").color(color(0x58ec7f)))
        /*while (region.iterator().hasNext()) {
            p0.sendMessage(text().content("正在填充实体").color(color(0x58ec7f)))
            val location = region.iterator().next().toLocation(p0.world).toBlockCenter()
            p0.sendMessage(text().content("---Location:$location").color(color(0x58ec7f)))

            try {
                if (entityToSpawn != null) {
                    entityToSpawn!!.copy(location)
                    p0.sendMessage("copied entity to ${location.x}, ${location.y}, ${location.z}")
                } else {
                    p0.sendMessage("entity initialized")
                    val entityPrime = p0.world.spawnEntity(location, ep.initEntity())
                    entityToSpawn = ep.writeAttribute(entityPrime)
                }
            } catch (e: Exception) {
                p0.sendMessage(text().content("发生了错误："))
                p0.sendMessage(text().content(e.message ?: "发生了未知错误").color(color(0x58ec7f)))
            }*/
        return true
    }


}