package ru.ifmo.sd.world.configuration

import ru.ifmo.sd.world.generation.LevelGenerator
import ru.ifmo.sd.world.representation.*
import ru.ifmo.sd.world.representation.units.Player

object GameConfigurator {
    private lateinit var gameLevel: GameLevel
    private lateinit var unitsPositions: UnitsPositionStorage
    private lateinit var unitsHealths: UnitsHealthStorage

    fun getGameLevel(): GameLevel {
        return gameLevel
    }

    fun getUnitsPositions(): UnitsPositionStorage {
        return unitsPositions
    }

    fun getUnitsHealths(): UnitsHealthStorage {
        return unitsHealths
    }

    fun configure(levelLength: Int, levelWidth: Int): GameConfiguration {
        gameLevel = GameLevel(LevelGenerator.generateLevel(
            levelLength, levelWidth))
        val playerId = 1
        val player = Player(playerId)
        unitsPositions = UnitsPositionStorage()
        unitsHealths = UnitsHealthStorage()
        unitsPositions.move(player, Position(1, 1))
        unitsHealths.addUnit(player)
        return GameConfiguration(gameLevel.gameLevel, unitsPositions.getPositions(), unitsHealths.getHealths())
    }
}
