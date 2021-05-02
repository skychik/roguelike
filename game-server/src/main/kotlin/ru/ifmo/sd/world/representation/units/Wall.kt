package ru.ifmo.sd.world.representation.units

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.InteractionExecutor
import ru.ifmo.sd.world.events.MazeEvent

class Wall : MazeObject {
    override fun interact(executor: InteractionExecutor, pos: Position): MutableSet<MazeEvent> {
        return executor.doFor(this, pos)
    }

    override fun getTypeIdentifier() = 1
}
