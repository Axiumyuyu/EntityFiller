package me.axiumyu

import org.bukkit.plugin.java.JavaPlugin.getPlugin

object ConfigFile {
    var maxFillCount = getPlugin(EntityFiller::class.java).config.get("max-fill-count") as Int
}