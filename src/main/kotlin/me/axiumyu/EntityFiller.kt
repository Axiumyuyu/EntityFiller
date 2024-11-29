package me.axiumyu

import me.axiumyu.commands.MainCommand
import me.axiumyu.commands.OnFill
import org.bukkit.Bukkit.getPluginManager
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Axium_yu
 */
class EntityFiller : JavaPlugin() {
    override fun onEnable() {
        getPluginManager().run {
            if (!(isPluginEnabled("WorldEdit") || isPluginEnabled("NBTAPI"))) {
                logger.warning("WorldEdit or NBTAPI is not installed or enabled!")
                disablePlugin(this@EntityFiller)
            }
        }
        mapOf("fillentity" to OnFill, "ef" to MainCommand).forEach { (cmd, exec) ->
            getCommand(cmd)?.setExecutor(exec)
        }
        saveDefaultConfig()
        ConfigFile.maxFillCount = config.getInt("max-fill-count", 100)
    }

    override fun onDisable() {
        super.onDisable()
    }
}
