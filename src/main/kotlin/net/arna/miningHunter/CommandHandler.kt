package net.arna.miningHunter

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

object CommandHandler : TabExecutor
{
    private val game = MiningHunter.GameManager
    private val keys = arrayListOf(
        "time",
        "ore_coal",
        "ore_copper",
        "ore_iron",
        "ore_gold",
        "ore_lapis",
        "ore_redstone",
        "ore_diamond",
        "ore_emerald",
        "nop_chance",
        "score",
        "explode_power",
        "explosion_drop",
        "teleport_distance",
        "show")

    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, args: Array<out String>): MutableList<String>?
    {
        if (args == null)
            return arrayListOf()
        else
        if (args.size == 1)
            return arrayListOf("start", "stop", "config")
        else
        if (args.size == 2 && args[0] == "config")
            return keys

        return arrayListOf()
    }

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>): Boolean
    {
        if (args == null) return false
        if (args.isEmpty()) return false
        if (args.size == 1)
        {
            val option = args[0]

            if (option == "start")
            {
                game.start()
            }
            else
            if (option == "stop")
            {
                game.stop()
            }
        }
        else
        {
            val option = args[0]

            if (option == "config")
            {
                val cfg = MiningHunter.ConfigManager

                if (args.size != 2 && args.size != 3) return false
                if (args[1] == "show")
                {
                    var text = "Configurations\n"
                    for (k in keys)
                    {
                        if (k == "show") continue
                        text += "$k : ${cfg.getValue(k)}\n"
                    }
                    sender.sendMessage(text)
                    return true
                }

                cfg.setValue(args[1], args[2] as Any)
                sender.sendMessage("[Config] ${args[1]} has been changed to ${cfg.getValue(args[1])}")
            }
        }

        return true
    }
}