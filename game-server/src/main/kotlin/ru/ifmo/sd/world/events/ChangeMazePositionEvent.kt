package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.representation.units.MazeObject

/**
 * Класс события изменения игрового объекта на заданной позиции.
 */
data class ChangeMazePositionEvent(val position: Position, val newMazeObj: MazeObject?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChangeMazePositionEvent

        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }
}
