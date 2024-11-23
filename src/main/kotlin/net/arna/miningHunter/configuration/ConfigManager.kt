package net.arna.miningHunter.configuration

import net.arna.miningHunter.MiningHunter

class ConfigManager(
    private val plugin : MiningHunter)
{
    init {
        plugin.saveDefaultConfig()
    }

    fun setValue(key: String, value: Any)
    {
        plugin.config[key] = value
        plugin.saveConfig()
    }

    fun getValue(key: String) : String
    {
        return plugin.config[key] as String
    }

}