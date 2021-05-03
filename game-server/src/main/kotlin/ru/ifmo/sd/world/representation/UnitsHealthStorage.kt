package ru.ifmo.sd.world.representation

import ru.ifmo.sd.httpapi.models.Position


/**
 * Хранилище информации о жизненных силах персонажа и NPC.
 */
class UnitsHealthStorage {
    private val healths: MutableMap<Position, Int> = HashMap()

    /**
     * Возвращает словарь жизненных сил юнитов.
     *
     * @return словарь с жизненными силами юнитов
     */
    fun getHealths(): Map<Position, Int> {
        return healths
    }

    /**
     * Добавляет в хранилище нового юнита на заданную
     * позицию с заданным количеством жизненных сил.
     *
     * @param pos -- позиция нового юнита
     * @param value -- количество жизненных сил нового юнита
     */
    fun addUnit(pos: Position, value: Int = 100) {
        healths[pos] = value
    }

    /**
     * Уничтожает юнита на заданной позиции.
     *
     * @param pos -- позиция юнита
     */
    fun eliminateUnit(pos: Position) {
        healths.remove(pos)
    }

    /**
     * Увеличивает количество жизненных сил юнита на
     * заданной позиции на заданное количество единиц.
     *
     * @param pos -- позиция юнита
     * @param value -- количество жизненных сил
     */
    fun increase(pos: Position, value: Int) {
        healths.merge(pos, value, Int::plus)
    }

    /**
     * Уменьшает количество жизненных сил юнита на
     * заданной позиции на заданное количество единиц.
     *
     * @param pos -- позиция юнита
     * @param value -- количество жизненных сил
     */
    fun decrease(pos: Position, value: Int) {
        healths.merge(pos, value, Int::minus)
    }

    /**
     * Проверяет, что юнит на заданной позиции жив.
     *
     * @param pos -- позиция юнита
     * @return флаг, соответствующий состоянию юнита
     */
    fun isAlive(pos: Position): Boolean {
        return healths.getOrDefault(pos, 0) > 0
    }
}
