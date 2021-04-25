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
        val player = Player(1)
        unitsPositions.move(player, Position(0, 0))
        unitsHealths.addUnit(player)
        return GameConfiguration(gameLevel, unitsPositions, unitsHealths)
    }
}