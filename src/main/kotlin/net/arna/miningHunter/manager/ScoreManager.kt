package net.arna.miningHunter.manager

import net.arna.miningHunter.data.IndicatingScoreData
import org.bukkit.entity.Player
import java.util.*

class ScoreManager
{
    private var score = 0
    private var indicatingScore: MutableMap<UUID, IndicatingScoreData?> = mutableMapOf()

    fun register(player: Collection<Player>)
    {
        player.forEach {
            indicatingScore[it.uniqueId] = null
        }
    }

    fun increment(value: Int)
    {
        score += value
    }

    fun getScore() : Int
    {
        return score
    }

    fun dispose()
    {
        score = 0
    }

    fun getIndicatingScore(uid: UUID) : Int
    {
        val v = indicatingScore.getValue(uid)
        return v?.score ?: -1
    }

    fun updateIndicatingFrame(uid: UUID)
    {
        indicatingScore[uid]!!.frame++
        if (indicatingScore[uid]!!.frame == 30) setIndicatingData(uid, null)
    }

    fun setIndicatingData(uid: UUID, data: IndicatingScoreData?)
    {
        if (indicatingScore[uid] != null && data != null)
        {
            indicatingScore[uid]!!.score += data.score
            indicatingScore[uid]!!.frame = 0
            return
        }
        indicatingScore[uid] = data
    }
}