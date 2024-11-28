package me.axiumyu

import me.axiumyu.commands.MainCommand
import me.axiumyu.commands.OnFill
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
/**
 * @author Axium_yu
 */
class EntityFiller : JavaPlugin() {
    override fun onEnable() {
        val manager = Bukkit.getPluginManager()
        if (!(manager.isPluginEnabled("WorldEdit") || manager.isPluginEnabled("NBTAPI"))) {
            logger.warning("WorldEdit or NBTAPI is not installed or enabled!")
            manager.disablePlugin(this)
        }
        getCommand("fillentity")?.setExecutor(OnFill)
        getCommand("ef")?.setExecutor(MainCommand)
    }

    override fun onDisable() {
        super.onDisable()
    }
}
