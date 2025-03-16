package net.arna.miningHunter.listener

import net.arna.miningHunter.MiningHunter
import net.arna.miningHunter.data.IndicatingScoreData
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

object EntityPickupItemEventListener : Listener
{
    private val game = MiningHunter.GameManager

    @EventHandler
    fun onPickup(e: EntityPickupItemEvent)
    {
        if (!game.started) return
        if (e.entity !is Player) return
        val p = e.entity as Player
        if (!game.playerManager.getPlayers().contains(p)) return

        val score = getScore(e.item.itemStack.type)
        if (score == -1) return

        if (e.item.hasMetadata("ore_drop"))
        {
            val meta = e.item.getMetadata("ore_drop").firstOrNull()
            if (meta != null && meta.asBoolean())
            {
                game.scoreManager.increment(p.uniqueId,score * e.item.itemStack.amount)
                game.scoreManager.setIndicatingData(
                    p.uniqueId, IndicatingScoreData(score * e.item.itemStack.amount, 0))
            }
        }
    }
    

    private fun getScore(mat: Material) : Int
    {
        return when(mat)
        {
            Material.COAL -> game.config.coalScore
            Material.RAW_COPPER -> game.config.copperScore
            Material.RAW_IRON -> game.config.ironScore
            Material.RAW_GOLD -> game.config.goldScore
            Material.LAPIS_LAZULI -> game.config.lapisScore
            Material.REDSTONE -> game.config.redstoneScore
            Material.DIAMOND -> game.config.diamondScore
            Material.EMERALD -> game.config.emeraldScore
            else -> -1
        }
    }
}