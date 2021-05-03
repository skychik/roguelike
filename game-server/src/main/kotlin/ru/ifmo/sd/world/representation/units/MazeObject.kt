package ru.ifmo.sd.world.representation.units

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.InteractionExecutor
import ru.ifmo.sd.world.events.ChangeMazePositionEvent


/**
 * Интерфейс объекта игрового лабиринта.
 */
interface MazeObject {
    /**
     * Запускает взаимодействие с текущим объектом с помощью исполнителя взаимодействия.
     *
     * @param executor -- исполнитель взаимодействия
     * @param mazeObjPos -- позиция текущего объекта
     * @return множество событий изменения лабиринта
     */
    fun interact(executor: InteractionExecutor, mazeObjPos: Position): MutableSet<ChangeMazePositionEvent>

    /**
     * Возвращает идентификатор типа текущего объекта.
     *
     * @return идентификатор типа
     */
    fun getTypeIdentifier(): Int
}
