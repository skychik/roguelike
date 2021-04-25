package ru.ifmo.sd.world.configuration

import kotlinx.serialization.Serializable
import ru.ifmo.sd.world.representation.*
import ru.ifmo.sd.world.representation.units.GameUnit

@Serializable
data class GameConfiguration(
    val playerPos: Position,
    val level: Array<IntArray>,
    val unitsPositions: Map<GameUnit, Position>,
    val unitsHealthStorage: Map<GameUnit, Int>
)
