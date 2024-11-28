package me.axiumyu.entityFiller

import me.axiumyu.entityFiller.commands.OnFill
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class EntityFiller : JavaPlugin() {
    /**
     * @author Axium_yu
     * @date 2024/11/26 16:41
     * @description 填充实体，就像填充方块那样
     */

    override fun onEnable() {
        if (!(Bukkit.getPluginManager().isPluginEnabled("WorldEdit") || Bukkit.getPluginManager()
                .isPluginEnabled("NBTAPI"))) {
            logger.warning("WorldEdit or NBTAPI is not installed or enabled!")
            Bukkit.getPluginManager().disablePlugin(this)
        }
        getCommand("fillentity")?.setExecutor(OnFill)
    }

    override fun onDisable() {
        super.onDisable()

    }
}
