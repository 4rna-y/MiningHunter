package net.arna.miningHunter.tick

import net.arna.miningHunter.MiningHunter
import net.arna.miningHunter.manager.GameManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Sound
import java.time.Duration

object BeginningCountDownTickHandler : Runnable
{
    private val game: GameManager = MiningHunter.GameManager
    private var frame = 0

    override fun run()
    {
        if (frame < 20 * 3)
        {
            if (frame % 20 == 0)
            {
                game.playerManager.getPlayers().forEach {
                    it.sendTitlePart(
                        TitlePart.TITLE,
                        Component.text((60 - frame) / 20))
                    it.sendTitlePart(
                        TitlePart.TIMES,
                        Title.Times.times(
                            Duration.ZERO,
                            Duration.ofMillis(750),
                            Duration.ofMillis(250)
                        ))
                    it.playSound(it, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1f, 1f)
                }
            }
            frame++
        }
        else
        if (frame == 60)
        {
            game.playerManager.getPlayers().forEach {
                it.sendTitlePart(
                    TitlePart.TITLE,
                    Component.text("スタート！！")
                        .color(TextColor.color(255, 215, 0)))
                it.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(2500),
                        Duration.ofMillis(250)
                    ))
                it.playSound(it, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1f, 1f)
            }
            frame = 0
            game.timeManager.unregisterTickHandler(game.timeManager.beginningCountDownTickScheduleId)
            game.timeManager.inGameTimerTickScheduleId = game.timeManager.registerTickHandler(InGameTimeTickHandler)
        }
    }
}