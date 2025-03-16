package net.arna.miningHunter.manager

import org.bukkit.entity.Player
import java.rmi.server.UID
import java.util.*

class PlayerManager
{
    private var players: MutableCollection<Player> = arrayListOf()

    fun setPlayers(players: Collection<Player>)
    {
        this.players = players.toMutableList()
    }

    fun getPlayers() : Collection<Player>
    {
        return players
    }

    fun getPlayer(uid: UUID) : Player?
    {
        return players.firstOrNull{ it.uniqueId == uid }
    }

    fun getPlayers(tuple: Map<UUID, Int>) : Map<Player, Int>
    {
        val dest = mutableMapOf<Player, Int>()
        tuple.forEach { (k, v) ->
            dest[getPlayer(k)!!] = v
        }

        return dest
    }

    fun dispose()
    {
        players.clear()
    }

}