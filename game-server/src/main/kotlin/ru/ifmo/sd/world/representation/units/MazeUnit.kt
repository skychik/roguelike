package ru.ifmo.sd.world.representation.units

import kotlinx.serialization.Serializable
import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.InteractionExecutor
import ru.ifmo.sd.world.events.ChangeMazePositionEvent

interface MazeUnit : MazeObject

@Serializable
class Player : MazeUnit {
    override fun interact(executor: InteractionExecutor, mazeObjPos: Position): MutableSet<ChangeMazePositionEvent> {
        return executor.doFor(this, mazeObjPos)
    }

    override fun getTypeIdentifier() = 2
}

@Serializable
class Enemy : MazeUnit {
    override fun interact(executor: InteractionExecutor, mazeObjPos: Position): MutableSet<ChangeMazePositionEvent> {
        return executor.doFor(this, mazeObjPos)
    }

    override fun getTypeIdentifier() = 3
}
