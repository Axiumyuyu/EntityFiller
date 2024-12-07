package me.axiumyu.commands

import me.axiumyu.ConfigFile
import me.axiumyu.EntityFiller
import me.axiumyu.commands.OnFill.ERROR
import me.axiumyu.commands.OnFill.INFO
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor.color
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
            p0.sendMessage(text().content("You do not have permission to use this command.").color(color(ERROR)))
            return true
        }
        if (p3 == null || p3.isEmpty()) {
            p0.sendMessage(text().content("§cPlease specify a subcommand.").color(color(ERROR)))
            return false
        }
        when (p3[0]) {
            "reload" -> {
                getPlugin(EntityFiller::class.java).reloadConfig()
                ConfigFile.maxFillCount = getPlugin(EntityFiller::class.java).config.getInt("max-fill-count")
                p0.sendMessage(text().content("Config reloaded.").color(color(INFO)))
                return true
            }
            else -> {}
        }
        return true
    }
}