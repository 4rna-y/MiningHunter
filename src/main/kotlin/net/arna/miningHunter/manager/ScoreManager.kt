package net.arna.miningHunter.manager

import net.arna.miningHunter.data.IndicatingScoreData
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.ScoreboardManager
import java.util.*

class ScoreManager
{
    private var scores: MutableMap<UUID, Int> = mutableMapOf()
    private var indicatingScore: MutableMap<UUID, IndicatingScoreData?> = mutableMapOf()
    private var scManager: ScoreboardManager = Bukkit.getScoreboardManager()
    private var sc: Scoreboard = scManager.newScoreboard

    fun register(player: Collection<Player>)
    {
        player.forEach {
            it.scoreboard = scManager.mainScoreboard
            indicatingScore[it.uniqueId] = null
            scores[it.uniqueId] = 0
        }
    }

    fun increment(uid: UUID, value: Int)
    {
        if (scores[uid] == null) return
        scores[uid] = scores[uid]!! + value
    }

    fun getScore(uid: UUID) : Int
    {
        return scores[uid]!!
    }

    fun dispose()
    {
        scores.clear()
    }

    fun checkScores(goalScore: Int): UUID?
    {
        var res: UUID? = null
        scores.forEach { k, v ->
            if (goalScore <= v)
            {
                res = k
            }
        }

        return res
    }

    fun getRankSorted() : Map<UUID, Int>
    {
        return scores.toList().sortedBy { it.second }.toMap()
    }

    fun getScoreBoard(tuple: Map<Player, Int>): Scoreboard
    {
        sc = scManager.newScoreboard
        val o = sc.registerNewObjective("score", "score", "順位")
        o.displaySlot = DisplaySlot.SIDEBAR
        tuple.forEach { (k, v) ->
            val s = o.getScore("1位. " + k.name)
            s.score = v
        }

        return sc
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