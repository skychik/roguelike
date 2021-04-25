package ru.ifmo.sd.world.representation

import ru.ifmo.sd.world.representation.units.GameUnit

class UnitsPositionStorage {
    private val positions: MutableMap<GameUnit, Position> = HashMap()

    fun getPositions() = this.positions

    fun move(targetGameUnit: GameUnit, newPos: Position) {
        positions[targetGameUnit] = newPos
    }

    fun eliminateUnit(targetGameUnit: GameUnit) {
        positions.remove(targetGameUnit)
    }
}
