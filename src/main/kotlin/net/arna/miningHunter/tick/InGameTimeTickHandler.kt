package net.arna.miningHunter.tick

import net.arna.miningHunter.MiningHunter
import net.arna.miningHunter.manager.GameManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import java.time.Duration

object InGameTimeTickHandler : Runnable
{
    private val game: GameManager = MiningHunter.GameManager
    private var frame = 0
    private var bossBar: BossBar = Bukkit.createBossBar(
        "残り: ", BarColor.GREEN, BarStyle.SOLID)

    override fun run()
    {
        val limitTick = game.config.limitTime * 20 * 60
        if (frame == 0)
        {
            game.playerManager.getPlayers().forEach {
                bossBar.addPlayer(it)
            }
        }

        val elapsed = limitTick - frame
        if (elapsed % 20 == 0)
        {
            val min = (elapsed / 20) / 60
            val sec = (elapsed / 20) % 60
            bossBar.setTitle("残り: ${min}m${sec}s")
        }
        bossBar.progress = (elapsed.toDouble() / limitTick.toDouble())

        game.playerManager.getPlayers().forEach {
            val indScore = game.scoreManager.getIndicatingScore(it.uniqueId)
            val baseText = Component.text(
                "スコア: ${game.scoreManager.getScore()} / ${game.config.goalScore}")
            var t = baseText
            if (indScore != -1)
            {
                t = baseText.append(
                    Component.text(" +${indScore}")
                        .color(TextColor.color(150, 150, 150))
                )
                game.scoreManager.updateIndicatingFrame(it.uniqueId)
            }

            it.sendActionBar(t)
        }

        if (game.scoreManager.getScore() >= game.config.goalScore)
        {
            frame = 0
            game.playerManager.getPlayers().forEach {
                bossBar.removePlayer(it)
                it.sendTitlePart(
                    TitlePart.TITLE,
                    Component.text("目標に到達した！")
                        .color(TextColor.color(255, 255, 0)))
                it.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(2500),
                        Duration.ofMillis(250)
                    ))
                it.playSound(it, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f)
            }
            game.timeManager.unregisterTickHandler(game.timeManager.inGameTimerTickScheduleId)
            game.started = false
            game.scoreManager.dispose()
            game.playerManager.dispose()
        }

        if (game.terminate)
        {
            frame = 0
            game.playerManager.getPlayers().forEach {
                bossBar.removePlayer(it)
                it.sendTitlePart(
                    TitlePart.TITLE,
                    Component.text("ゲームを強制終了しました")
                        .color(TextColor.color(255, 255, 255)))
                it.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(2500),
                        Duration.ofMillis(250)
                    ))
                it.playSound(it, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f)
            }
            game.timeManager.unregisterTickHandler(game.timeManager.inGameTimerTickScheduleId)
            game.started = false
            game.scoreManager.dispose()
            game.playerManager.dispose()
        }

        if (frame == limitTick)
        {
            frame = 0
            game.playerManager.getPlayers().forEach {
                bossBar.removePlayer(it)
                it.sendTitlePart(
                    TitlePart.TITLE,
                    Component.text("目標に届かなかった...")
                        .color(TextColor.color(255, 0, 0)))
                it.sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(2500),
                        Duration.ofMillis(250)
                    ))
                it.playSound(it, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f)
            }
            game.timeManager.unregisterTickHandler(game.timeManager.inGameTimerTickScheduleId)
            game.started = false
            game.scoreManager.dispose()
            game.playerManager.dispose()
        }

        frame++
    }
}