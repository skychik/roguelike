package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.representation.units.*


/**
 * Интерфейс классов, отвечающих за выполнение взаимодействия с объектом на заданной позиции.
 */
interface InteractionExecutor {
    /**
     * Выполняет взаимодействие со стеной.
     *
     * @param obj -- тип объекта
     * @param objPos -- позиция объекта
     * @return множество событий изменения лабиринта
     */
    fun doFor(obj: Wall, objPos: Position): MutableSet<ChangeMazePositionEvent>

    /**
     * Выполняет взаимодействие с противником.
     *
     * @param obj -- тип объекта
     * @param objPos -- позиция объекта
     * @return множество событий изменения лабиринта
     */
    fun doFor(obj: Enemy, objPos: Position): MutableSet<ChangeMazePositionEvent>

    /**
     * Выполнеяет взаимодействие с игроком.
     *
     * @param obj -- тип объекта
     * @param objPos -- позиция объекта
     * @return множество событий изменения лабиринта
     */
    fun doFor(obj: Player, objPos: Position): MutableSet<ChangeMazePositionEvent>
}

class InteractionExecutorImpl : InteractionExecutor {
    companion object {
        const val playerDamage = 25
        const val npcDamage = 10
    }

    override fun doFor(obj: Wall, objPos: Position): MutableSet<ChangeMazePositionEvent> {
        return HashSet()
    }

    override fun doFor(obj: Enemy, objPos: Position): MutableSet<ChangeMazePositionEvent> {
        val unitsHealthStorage = EventsHandler.gameLevel!!.unitsHealthStorage
        unitsHealthStorage.decrease(objPos, playerDamage)
        return if (!unitsHealthStorage.isAlive(objPos)) {
            unitsHealthStorage.eliminateUnit(objPos)
            EventsHandler.gameLevel!!.npcEventProvider.eliminateNpc(objPos)
            mutableSetOf(ChangeMazePositionEvent(objPos, null))
        } else {
            HashSet()
        }
    }

    override fun doFor(obj: Player, objPos: Position): MutableSet<ChangeMazePositionEvent> {
        val unitsHealthStorage = EventsHandler.gameLevel!!.unitsHealthStorage
        unitsHealthStorage.decrease(objPos, npcDamage)
        return if (!unitsHealthStorage.isAlive(objPos)) {
            unitsHealthStorage.eliminateUnit(objPos)
            mutableSetOf(ChangeMazePositionEvent(objPos, null))
        } else {
            HashSet()
        }
    }
}
