package ru.ifmo.sd.world.representation

import ru.ifmo.sd.world.representation.units.GameUnit

class UnitsPositionStorage {
    private val positionStorage: MutableMap<GameUnit, Position> = HashMap()

    fun move(targetGameUnit: GameUnit, newPos: Position) {
        positionStorage[targetGameUnit] = newPos
    }

    fun eliminateUnit(targetGameUnit: GameUnit) {
        positionStorage.remove(targetGameUnit)
    }
}
