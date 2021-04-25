package ru.ifmo.sd.world.configuration

import ru.ifmo.sd.world.representation.*

data class GameConfiguration(
    val level: GameLevel,
    val unitsPositions: UnitsPositionStorage,
    val unitsHealthStorage: UnitsHealthStorage
)
