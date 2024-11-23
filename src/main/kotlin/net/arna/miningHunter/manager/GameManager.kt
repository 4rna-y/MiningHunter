package net.arna.miningHunter.manager

import net.arna.miningHunter.MiningHunter
import net.arna.miningHunter.configuration.InGameConfig

class GameManager(private val plugin: MiningHunter)
{
    var started = false
    var terminate = false

    val playerManager = PlayerManager()
    val scoreManager = ScoreManager()
    val timeManager = TimeManager()
    lateinit var config : InGameConfig

    fun start()
    {
        if (started) return
        started = true

        config = InGameConfig(MiningHunter.ConfigManager)
        playerManager.setPlayers(plugin.server.onlinePlayers)
        scoreManager.register(playerManager.getPlayers())
        timeManager.start()

        return
    }

    fun stop()
    {
        terminate = true
    }
}