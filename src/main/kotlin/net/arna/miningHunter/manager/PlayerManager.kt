package net.arna.miningHunter.manager

import org.bukkit.entity.Player

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

    fun dispose()
    {
        players.clear()
    }

}