package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable
import ru.ifmo.sd.world.representation.Position
import ru.ifmo.sd.world.representation.units.GameUnit

@Serializable
data class MoveEventData(val targetUnit: GameUnit, val newPos: Position)
