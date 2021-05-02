package ru.ifmo.sd.world.representation.units

import kotlinx.serialization.Serializable
import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.InteractionExecutor
import ru.ifmo.sd.world.events.MazeEvent

interface MazeUnit : MazeObject

@Serializable
class Player : MazeUnit {
    override fun interact(executor: InteractionExecutor, pos: Position): MutableSet<MazeEvent> {
        return executor.doFor(this, pos)
    }

    override fun getTypeIdentifier() = 2
}

@Serializable
class Enemy : MazeUnit {
    override fun interact(executor: InteractionExecutor, pos: Position): MutableSet<MazeEvent> {
        return executor.doFor(this, pos)
    }

    override fun getTypeIdentifier() = 3
}
