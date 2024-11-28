package me.axiumyu

import me.axiumyu.commands.OnFill
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
/**
 * @author Axium_yu
 * @date 2024/11/26 16:41
 * @description 填充实体，就像填充方块那样
 */
class EntityFiller : JavaPlugin() {
    override fun onEnable() {
        val manager = Bukkit.getPluginManager()
        if (!(manager.isPluginEnabled("WorldEdit") || manager.isPluginEnabled("NBTAPI"))) {
            logger.warning("WorldEdit or NBTAPI is not installed or enabled!")
            manager.disablePlugin(this)
        }
        getCommand("fillentity")?.setExecutor(OnFill)
    }

    override fun onDisable() {
        super.onDisable()

    }
}
