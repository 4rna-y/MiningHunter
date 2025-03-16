package net.arna.miningHunter.configuration

class InGameConfig(cfg: ConfigManager)
{
    val limitTime = cfg.getValue("time").toInt()
    val goalScore = cfg.getValue("score").toInt()
    val coalScore = cfg.getValue("ore_coal").toInt()
    val copperScore = cfg.getValue("ore_copper").toInt()
    val ironScore = cfg.getValue("ore_iron").toInt()
    val goldScore = cfg.getValue("ore_gold").toInt()
    val lapisScore = cfg.getValue("ore_lapis").toInt()
    val redstoneScore = cfg.getValue("ore_redstone").toInt()
    val diamondScore = cfg.getValue("ore_diamond").toInt()
    val emeraldScore = cfg.getValue("ore_emerald").toInt()
    val nonOperationChance = cfg.getValue("nop_chance").toDouble()
    val explosionPower = cfg.getValue("explode_power").toFloat()
    val explosionDrop = cfg.getValue("explosion_drop").toBoolean()
    val teleportDistance = cfg.getValue("teleport_distance").toInt()
}