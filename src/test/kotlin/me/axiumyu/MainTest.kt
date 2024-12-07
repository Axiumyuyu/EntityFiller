package me.axiumyu

import com.sk89q.worldedit.bukkit.WorldEditPlugin
import de.tr7zw.nbtapi.plugin.NBTAPI
import org.bukkit.entity.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockbukkit.mockbukkit.MockBukkit


class MainTest {
    companion object{

    }

    @BeforeEach
    fun setup() {
        val server = MockBukkit.mock()
        val we = MockBukkit.load(WorldEditPlugin::class.java)
        val nbtapi = MockBukkit.load(NBTAPI::class.java)
        val plugin = MockBukkit.load(EntityFiller::class.java)
        val player : Player = server.addPlayer("testPlayer")
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }
}