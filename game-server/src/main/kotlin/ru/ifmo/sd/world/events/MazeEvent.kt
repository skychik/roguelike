package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.representation.units.MazeObject


data class MazeEvent(val position: Position, val newMazeObj: MazeObject?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MazeEvent

        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }
}
