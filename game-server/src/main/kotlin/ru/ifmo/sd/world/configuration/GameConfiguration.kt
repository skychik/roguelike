package ru.ifmo.sd.world.configuration

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ru.ifmo.sd.world.representation.*
import ru.ifmo.sd.world.representation.units.GameUnit

@Serializable
data class GameConfiguration(
    val level: Array<IntArray>,
    val unitsPositions: Map<@Contextual GameUnit, @Contextual Position>,
    val unitsHealthStorage: Map<@Contextual GameUnit, Int>
)
