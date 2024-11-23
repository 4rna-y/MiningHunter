package net.arna.miningHunter.manager

import net.arna.miningHunter.MiningHunter
import net.arna.miningHunter.tick.BeginningCountDownTickHandler

class TimeManager
{
    private val plugin : MiningHunter = MiningHunter.Instance

    var beginningCountDownTickScheduleId = -1;
    var inGameTimerTickScheduleId = -1;

    fun start()
    {
        beginningCountDownTickScheduleId = registerTickHandler(BeginningCountDownTickHandler);
    }

    fun registerTickHandler(runnable: Runnable) : Int
    {
        return plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, runnable, 0, 1)
    }

    fun unregisterTickHandler(id: Int)
    {
        plugin.server.scheduler.cancelTask(id)
    }
}