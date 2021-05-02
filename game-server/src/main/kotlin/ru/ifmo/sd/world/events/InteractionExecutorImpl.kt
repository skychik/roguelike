package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.representation.units.*


interface InteractionExecutor {
    fun doFor(obj: Wall, objPos: Position): MutableSet<MazeEvent>
    fun doFor(obj: Enemy, objPos: Position): MutableSet<MazeEvent>
    fun doFor(obj: Player, objPos: Position): MutableSet<MazeEvent>
}

class InteractionExecutorImpl : InteractionExecutor {
    companion object {
        const val playerDamage = 25
        const val npcDamage = 10
    }

    override fun doFor(obj: Wall, objPos: Position): MutableSet<MazeEvent> {
        return HashSet()
    }

    override fun doFor(obj: Enemy, objPos: Position): MutableSet<MazeEvent> {
        val unitsHealthStorage = EventsHandler.gameLevel!!.unitsHealthStorage
        unitsHealthStorage.decrease(objPos, playerDamage)
        return if (!unitsHealthStorage.isAlive(objPos)) {
            unitsHealthStorage.eliminateUnit(objPos)
            EventsHandler.gameLevel!!.npcEventProvider.eliminateNpc(objPos)
            mutableSetOf(MazeEvent(objPos, null))
        } else {
            HashSet()
        }
    }

    override fun doFor(obj: Player, objPos: Position): MutableSet<MazeEvent> {
        val unitsHealthStorage = EventsHandler.gameLevel!!.unitsHealthStorage
        unitsHealthStorage.decrease(objPos, npcDamage)
        return if (!unitsHealthStorage.isAlive(objPos)) {
            unitsHealthStorage.eliminateUnit(objPos)
            mutableSetOf(MazeEvent(objPos, null))
        } else {
            HashSet()
        }
    }
}
