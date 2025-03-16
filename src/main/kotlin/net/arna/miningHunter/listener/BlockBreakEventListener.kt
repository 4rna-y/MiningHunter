package net.arna.miningHunter.listener

import net.arna.miningHunter.MiningHunter
import net.arna.miningHunter.data.HardBlockData
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.ExplosionPrimeEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.util.Vector
import kotlin.random.Random

object BlockBreakEventListener : Listener
{
    private val game = MiningHunter.GameManager
    private val hardBlocks: MutableMap<Location, HardBlockData> = mutableMapOf()

    @EventHandler
    fun onBreak(e: BlockBreakEvent)
    {
        if (!game.started) return
        if (!game.playerManager.getPlayers().contains(e.player)) return

        val score = isScored(e.block.type)
        if (!score) return

        if (hardBlocks.containsKey(e.block.location))
        {
            cancelHardBlock(e)
            return
        }

        val r = Random.nextInt(0, 100)
        if (r < 100 * game.config.nonOperationChance)
        {
            dropWithMetaData(e)
            return
        }

        when (Random.nextInt(0,4))
        {
            0 -> explode(e)
            1 -> cancelItemDrop(e)
            2 -> dropRandomLocation(e)
            3 -> cancelHardBlock(e)
        }
    }

    @EventHandler
    fun onExplode(e: EntityExplodeEvent)
    {
        if (!game.started) return
        e.blockList().removeIf {
            val s = isScored(it.type)
            if (s) it.type = Material.AIR
            s
        }
    }

    private fun explode(e: BlockBreakEvent)
    {
        e.isCancelled = !game.config.explosionDrop
        e.isDropItems = false
        e.block.world.createExplosion(e.block.location, game.config.explosionPower, false, false)
        if (game.config.explosionDrop)
        {
            dropWithMetaData(e)
        }
    }

    private fun cancelItemDrop(e: BlockBreakEvent)
    {
        e.isDropItems = false
        e.player.sendMessage("鉱石がドロップしなかった...")
    }

    private fun dropRandomLocation(e: BlockBreakEvent)
    {
        val block = e.block
        val drops = block.drops

        e.isDropItems = false
        val dist = game.config.teleportDistance.toDouble()

        drops.forEach { itemStack ->
            val world = block.world
            val randomOffsetX = Random.nextDouble(-dist, dist)
            val randomOffsetY = Random.nextDouble(0.0, dist)
            val randomOffsetZ = Random.nextDouble(-dist, dist)

            val dropLocation = block.location.add(Vector(randomOffsetX, randomOffsetY, randomOffsetZ))

            val item = world.dropItem(dropLocation, itemStack)
            item.setMetadata("ore_drop", FixedMetadataValue(MiningHunter.Instance, true))
        }
        e.player.sendMessage("鉱石がどこかに飛んでいってしまった...")
    }

    private fun cancelHardBlock(e: BlockBreakEvent)
    {
        val block = e.block
        if (!hardBlocks.containsKey(block.location))
        {
            hardBlocks[block.location] = HardBlockData(0, Random.nextInt(1,4))
            e.isCancelled = true
            e.player.sendMessage("鉱石が硬すぎる...")
            return
        }
        if (hardBlocks[block.location]!!.count != hardBlocks[block.location]!!.max)
        {
            hardBlocks[block.location]!!.count++
            e.isCancelled = true
            e.player.sendMessage("鉱石が硬すぎる...")
            return
        }

        hardBlocks.remove(block.location)
        dropWithMetaData(e)
    }

    private fun dropWithMetaData(e: BlockBreakEvent)
    {
        e.block.drops.forEach { itemStack ->
            val droppedItem = e.block.world.dropItem(e.block.location, itemStack)
            droppedItem.setMetadata("ore_drop", FixedMetadataValue(MiningHunter.Instance, true))
        }

        e.isDropItems = false
    }

    private fun isScored(mat: Material) : Boolean
    {
        return when(mat)
        {
            Material.COAL_ORE -> true
            Material.DEEPSLATE_COAL_ORE -> true
            Material.COPPER_ORE -> true
            Material.DEEPSLATE_COPPER_ORE -> true
            Material.IRON_ORE -> true
            Material.DEEPSLATE_IRON_ORE -> true
            Material.GOLD_ORE -> true
            Material.DEEPSLATE_GOLD_ORE -> true
            Material.LAPIS_ORE -> true
            Material.DEEPSLATE_LAPIS_ORE -> true
            Material.REDSTONE_ORE -> true
            Material.DEEPSLATE_REDSTONE_ORE -> true
            Material.DIAMOND_ORE -> true
            Material.DEEPSLATE_DIAMOND_ORE -> true
            Material.EMERALD_ORE -> true
            Material.DEEPSLATE_EMERALD_ORE -> true
            else -> false
        }
    }
}