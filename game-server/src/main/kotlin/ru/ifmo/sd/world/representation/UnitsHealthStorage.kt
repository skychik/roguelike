package ru.ifmo.sd.world.representation

import ru.ifmo.sd.httpapi.models.Position

class UnitsHealthStorage {
    private val healths: MutableMap<Position, Int> = HashMap()

    fun addUnit(pos: Position, value: Int = 100) {
        healths[pos] = value
    }

    fun eliminateUnit(pos: Position) {
        healths.remove(pos)
    }

    fun increase(pos: Position, value: Int) {
        healths.merge(pos, value, Int::plus)
    }

    fun decrease(pos: Position, value: Int) {
        healths.merge(pos, value, Int::minus)
    }

    fun isAlive(pos: Position): Boolean {
        return healths.getOrDefault(pos, 0) > 0
    }
}
