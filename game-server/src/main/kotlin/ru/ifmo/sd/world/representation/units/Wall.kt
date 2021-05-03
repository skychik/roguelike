package ru.ifmo.sd.world.representation.units

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.InteractionExecutor
import ru.ifmo.sd.world.events.ChangeMazePositionEvent

class Wall : MazeObject {
    override fun interact(executor: InteractionExecutor, mazeObjPos: Position): MutableSet<ChangeMazePositionEvent> {
        return executor.doFor(this, mazeObjPos)
    }

    override fun getTypeIdentifier() = 1
}
