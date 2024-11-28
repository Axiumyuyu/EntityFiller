package me.axiumyu.commands

import me.axiumyu.ConfigFile
import me.axiumyu.EntityFiller
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin.getPlugin

object MainCommand : CommandExecutor {
    override fun onCommand(
        p0: CommandSender, p1: Command, p2: String,
        p3: Array<out String>?
    ): Boolean {
        if (!p0.hasPermission("entityfiller.command.admin")) {
            p0.sendMessage("§cYou do not have permission to use this command.")
            return true
        }
        if (p3 == null || p3.isEmpty()) {
            p0.sendMessage("§cPlease specify a subcommand.")
            return false
        }
        when (p3[0]) {
            "reload" -> {
                getPlugin(EntityFiller::class.java).reloadConfig()
                ConfigFile.maxFillCount = getPlugin(EntityFiller::class.java).config.getInt("max-fill-count")
            }
            else -> {}
        }
    }
}