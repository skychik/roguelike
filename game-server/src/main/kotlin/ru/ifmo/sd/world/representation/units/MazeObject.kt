package ru.ifmo.sd.world.representation.units

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.InteractionExecutor
import ru.ifmo.sd.world.events.MazeEvent

interface MazeObject {
    fun interact(executor: InteractionExecutor, pos: Position): MutableSet<MazeEvent>
    fun getTypeIdentifier(): Int
}
