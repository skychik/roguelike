package ru.ifmo.sd.world.representation

import ru.ifmo.sd.world.representation.units.GameUnit

class UnitsHealthStorage {
    private val healths: MutableMap<GameUnit, Int> = HashMap()

    fun getHealths() = this.healths

    fun addUnit(unit: GameUnit, value: Int = 100) {
        healths[unit] = value
    }

    fun increase(gameUnit: GameUnit, value: Int) {
        healths.merge(gameUnit, value, Int::plus)
    }

    fun decrease(gameUnit: GameUnit, value: Int) {
        healths.merge(gameUnit, value, Int::minus)
    }

    fun isAlive(gameUnit: GameUnit): Boolean {
        return healths.getOrDefault(gameUnit, 0) > 0
    }
}
