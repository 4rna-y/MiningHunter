package net.arna.miningHunter

import net.arna.miningHunter.configuration.ConfigManager
import net.arna.miningHunter.listener.BlockBreakEventListener
import net.arna.miningHunter.listener.EntityPickupItemEventListener
import net.arna.miningHunter.manager.GameManager
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger

class MiningHunter : JavaPlugin()
{
    companion object
    {
        lateinit var Instance: MiningHunter
            private set

        lateinit var ConfigManager: ConfigManager
            private set

        lateinit var GameManager: GameManager
            private set

        lateinit var Logger: Logger
            private set
    }


    override fun onEnable()
    {
        Instance = this
        ConfigManager = ConfigManager(this)
        GameManager = GameManager(this)
        Logger = this.slF4JLogger

        server.pluginManager.registerEvents(BlockBreakEventListener, this)
        server.pluginManager.registerEvents(EntityPickupItemEventListener, this)

        getCommand("mh")?.setExecutor(CommandHandler)
        getCommand("mh")?.tabCompleter = CommandHandler
    }
}
